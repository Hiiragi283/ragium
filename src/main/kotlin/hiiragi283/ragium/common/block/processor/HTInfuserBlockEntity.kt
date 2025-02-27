package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTInfuserRecipe
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.storage.HTFluidTank
import hiiragi283.ragium.api.storage.HTItemSlot
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.inventory.HTInfuserContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.state.BlockState

class HTInfuserBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.INFUSER, pos, state, HTMachineType.INFUSER) {
    private val inputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("item_input")
    private val outputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("item_output")

    private val inputTank: HTFluidTank = HTFluidTank
        .Builder()
        .setCallback(this::setChanged)
        .build("fluid_input")
    private val outputTank: HTFluidTank = HTFluidTank
        .Builder()
        .setCallback(this::setChanged)
        .build("fluid_output")

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, dynamicOps)
        inputSlot.writeNbt(nbt, dynamicOps)
        outputSlot.writeNbt(nbt, dynamicOps)

        inputTank.writeNbt(nbt, dynamicOps)
        outputTank.writeNbt(nbt, dynamicOps)
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.readNbt(nbt, dynamicOps)
        inputSlot.readNbt(nbt, dynamicOps)
        outputSlot.readNbt(nbt, dynamicOps)

        inputTank.readNbt(nbt, dynamicOps)
        outputTank.readNbt(nbt, dynamicOps)
    }

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.CHEMICAL

    override fun process(level: ServerLevel, pos: BlockPos) {
        // Find matching recipe
        val input: HTMachineRecipeInput = HTMachineRecipeInput
            .Builder()
            .addItem(inputSlot)
            .addFluid(inputTank)
            .build()
        val recipe: HTInfuserRecipe = HTRecipeTypes.INFUSER.getFirstRecipe(input, level).getOrThrow()
        // Try to insert outputs
        recipe.canInsert(this, intArrayOf(1), intArrayOf(1))
        // Insert outputs
        recipe.insertOutputs(this, intArrayOf(1), intArrayOf(1))
        // Decrement input
        if (!inputSlot.canShrink(recipe.itemInput.count, true)) throw HTMachineException.ShrinkInput(false)
        if (!inputTank.canShrink(recipe.fluidInput.amount(), true)) throw HTMachineException.ShrinkInput(false)

        inputSlot.shrinkStack(recipe.itemInput.count, false)
        inputTank.shrinkStack(recipe.fluidInput.amount(), false)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTInfuserContainerMenu(containerId, playerInventory, blockPos, inputSlot, outputSlot)

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        super.updateEnchantments(newEnchantments)
        inputTank.updateCapacity(this)
        outputTank.updateCapacity(this)
    }

    //    Item    //

    override fun getItemSlot(slot: Int): HTItemSlot? = when (slot) {
        0 -> inputSlot
        1 -> outputSlot
        else -> null
    }

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = when (slot) {
        0 -> HTStorageIO.INPUT
        1 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    override fun getSlots(): Int = 2

    //    Fluid    //

    override fun getFluidTank(tank: Int): HTFluidTank? = when (tank) {
        0 -> inputTank
        1 -> outputTank
        else -> null
    }

    override fun getFluidIoFromSlot(tank: Int): HTStorageIO = when (tank) {
        0 -> HTStorageIO.INPUT
        1 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    override fun getTanks(): Int = 2
}
