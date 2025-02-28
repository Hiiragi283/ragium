package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeContext
import hiiragi283.ragium.api.storage.HTFluidTank
import hiiragi283.ragium.api.storage.HTItemSlot
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.inventory.HTMixerContainerMenu
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

class HTMixerBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.MIXER, pos, state, HTMachineType.MIXER) {
    private val inputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("item_input")

    private val firstInputTank: HTFluidTank = HTFluidTank
        .Builder()
        .setCallback(this::setChanged)
        .build("first_fluid_input")
    private val secondInputTank: HTFluidTank = HTFluidTank
        .Builder()
        .setCallback(this::setChanged)
        .build("second_fluid_input")

    private val outputTank: HTFluidTank = HTFluidTank
        .Builder()
        .setCallback(this::setChanged)
        .build("fluid_output")

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, dynamicOps)
        inputSlot.writeNbt(nbt, dynamicOps)

        firstInputTank.writeNbt(nbt, dynamicOps)
        secondInputTank.writeNbt(nbt, dynamicOps)
        outputTank.writeNbt(nbt, dynamicOps)
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.readNbt(nbt, dynamicOps)
        inputSlot.readNbt(nbt, dynamicOps)

        firstInputTank.readNbt(nbt, dynamicOps)
        secondInputTank.readNbt(nbt, dynamicOps)
        outputTank.readNbt(nbt, dynamicOps)
    }

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.CHEMICAL

    override fun process(level: ServerLevel, pos: BlockPos) {
        val context: HTMachineRecipeContext = HTMachineRecipeContext
            .builder()
            .addInput(0, inputSlot)
            .addInput(0, firstInputTank)
            .addInput(1, secondInputTank)
            .addOutput(0, outputTank)
            .build()
        HTRecipeTypes.MIXER.processFirstRecipe(context, level)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTMixerContainerMenu(containerId, playerInventory, blockPos, inputSlot)

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        super.updateEnchantments(newEnchantments)
        firstInputTank.updateCapacity(this)
        secondInputTank.updateCapacity(this)
        outputTank.updateCapacity(this)
    }

    //    Item    //

    override fun getItemSlot(slot: Int): HTItemSlot? = inputSlot

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = HTStorageIO.INPUT

    override fun getSlots(): Int = 1

    //    Fluid    //

    override fun getFluidTank(tank: Int): HTFluidTank? = when (tank) {
        0 -> firstInputTank
        1 -> secondInputTank
        2 -> outputTank
        else -> null
    }

    override fun getFluidIoFromSlot(tank: Int): HTStorageIO = when (tank) {
        0 -> HTStorageIO.INPUT
        1 -> HTStorageIO.INPUT
        2 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    override fun getTanks(): Int = 3
}
