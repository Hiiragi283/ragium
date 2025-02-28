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
import hiiragi283.ragium.common.inventory.HTSolidifierContainerMenu
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

class HTSolidifierBlockEntity(pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(RagiumBlockEntityTypes.SOLIDIFIER, pos, state, HTMachineType.SOLIDIFIER) {
    private val inputTank: HTFluidTank = HTFluidTank
        .Builder()
        .setCallback(this::setChanged)
        .build("input")

    private val catalystSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCapacity(1)
        .setCallback(this::setChanged)
        .build("catalyst")
    private val outputSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCallback(this::setChanged)
        .build("output")

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, dynamicOps)
        inputTank.writeNbt(nbt, dynamicOps)
        catalystSlot.writeNbt(nbt, dynamicOps)
        outputSlot.writeNbt(nbt, dynamicOps)
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.readNbt(nbt, dynamicOps)
        inputTank.readNbt(nbt, dynamicOps)
        catalystSlot.readNbt(nbt, dynamicOps)
        outputSlot.readNbt(nbt, dynamicOps)
    }

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.CHEMICAL

    override fun process(level: ServerLevel, pos: BlockPos) {
        val context: HTMachineRecipeContext = HTMachineRecipeContext
            .builder()
            .addInput(0, inputTank)
            .addCatalyst(catalystSlot)
            .addOutput(0, outputSlot)
            .build()
        HTRecipeTypes.SOLIDIFIER.processFirstRecipe(context, level)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTSolidifierContainerMenu(containerId, playerInventory, blockPos, catalystSlot, outputSlot)

    override fun onUpdateEnchantment(newEnchantments: ItemEnchantments) {
        super.onUpdateEnchantment(newEnchantments)
        inputTank.onUpdateEnchantment(newEnchantments)
    }

    //    Item    //

    override fun getItemIoFromSlot(slot: Int): HTStorageIO = when (slot) {
        0 -> HTStorageIO.CATALYST
        1 -> HTStorageIO.OUTPUT
        else -> HTStorageIO.EMPTY
    }

    override fun getItemSlot(slot: Int): HTItemSlot? = when (slot) {
        0 -> catalystSlot
        1 -> outputSlot
        else -> null
    }

    override fun getSlots(): Int = 2

    //    Fluid    //

    override fun getFluidIoFromSlot(tank: Int): HTStorageIO = HTStorageIO.INPUT

    override fun getFluidTank(tank: Int): HTFluidTank? = inputTank

    override fun getTanks(): Int = 1
}
