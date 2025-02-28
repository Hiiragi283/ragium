package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeContext
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.inventory.HTRefineryContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
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

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, dynamicOps)
        outputSlot.writeNbt(nbt, dynamicOps)

        inputTank.writeNbt(nbt, dynamicOps)
        firstOutputTank.writeNbt(nbt, dynamicOps)
        secondOutputTank.writeNbt(nbt, dynamicOps)
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.readNbt(nbt, dynamicOps)
        outputSlot.readNbt(nbt, dynamicOps)

        inputTank.readNbt(nbt, dynamicOps)
        firstOutputTank.readNbt(nbt, dynamicOps)
        secondOutputTank.readNbt(nbt, dynamicOps)
    }

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.CHEMICAL

    override fun process(level: ServerLevel, pos: BlockPos) {
        val context: HTMachineRecipeContext = HTMachineRecipeContext
            .builder()
            .addInput(0, inputTank)
            .addOutput(0, outputSlot)
            .addOutput(0, firstOutputTank)
            .addOutput(1, secondOutputTank)
            .build()
        HTRecipeTypes.REFINERY.processFirstRecipe(context, level)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTRefineryContainerMenu(containerId, playerInventory, blockPos, outputSlot)

    override fun onUpdateEnchantment(newEnchantments: ItemEnchantments) {
        super.onUpdateEnchantment(newEnchantments)
        inputTank.onUpdateEnchantment(newEnchantments)
        firstOutputTank.onUpdateEnchantment(newEnchantments)
        secondOutputTank.onUpdateEnchantment(newEnchantments)
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        outputSlot.dropStack(level, pos)
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
