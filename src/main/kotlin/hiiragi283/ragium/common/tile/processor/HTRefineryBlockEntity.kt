package hiiragi283.ragium.common.tile.processor

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.api.recipe.HTRefineryRecipe
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeCache
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeContext
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.inventory.HTRefineryMenu
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.state.BlockState

class HTRefineryBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.REFINERY, pos, state, HTMachineType.REFINERY) {
    private val outputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("item")

    private val inputTank: HTFluidTank = HTFluidTank
        .Builder()
        .setCallback(this::setChanged)
        .build("fluid_input")
    private val firstOutputTank: HTFluidTank = HTFluidTank
        .Builder()
        .setCallback(this::setChanged)
        .build("first_fluid_output")
    private val secondOutputTank: HTFluidTank = HTFluidTank
        .Builder()
        .setCallback(this::setChanged)
        .build("second_fluid_output")

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, registryOps)
        outputSlot.writeNbt(nbt, registryOps)

        inputTank.writeNbt(nbt, registryOps)
        firstOutputTank.writeNbt(nbt, registryOps)
        secondOutputTank.writeNbt(nbt, registryOps)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.readNbt(nbt, registryOps)
        outputSlot.readNbt(nbt, registryOps)

        inputTank.readNbt(nbt, registryOps)
        firstOutputTank.readNbt(nbt, registryOps)
        secondOutputTank.readNbt(nbt, registryOps)
    }

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.CHEMICAL

    private val recipeCache: HTMachineRecipeCache<HTRefineryRecipe> = HTMachineRecipeCache(HTRecipeTypes.REFINERY)

    override fun process(level: ServerLevel, pos: BlockPos) {
        val context: HTMachineRecipeContext = HTMachineRecipeContext
            .builder()
            .addInput(0, inputTank)
            .addOutput(0, outputSlot)
            .addOutput(0, firstOutputTank)
            .addOutput(1, secondOutputTank)
            .build()
        recipeCache.processFirstRecipe(context, level)
    }

    override fun onSucceeded() {
        super.onSucceeded()
        playSound(SoundEvents.LAVA_POP)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTRefineryMenu(containerId, playerInventory, blockPos, outputSlot)

    override fun onUpdateEnchantment(newEnchantments: ItemEnchantments) {
        super.onUpdateEnchantment(newEnchantments)
        inputTank.onUpdateEnchantment(newEnchantments)
        firstOutputTank.onUpdateEnchantment(newEnchantments)
        secondOutputTank.onUpdateEnchantment(newEnchantments)
    }

    //    Item    //

    override fun getItemSlot(slot: Int): HTItemSlot? = outputSlot

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = HTStorageIO.OUTPUT

    override fun getSlots(): Int = 1

    //    Fluid    //

    override fun getFluidTank(tank: Int): HTFluidTank? = when (tank) {
        0 -> inputTank
        1 -> firstOutputTank
        2 -> secondOutputTank
        else -> null
    }

    override fun getFluidIoFromSlot(tank: Int): HTStorageIO = when (tank) {
        0 -> HTStorageIO.INPUT
        1 -> HTStorageIO.OUTPUT
        2 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    override fun getTanks(): Int = 3
}
