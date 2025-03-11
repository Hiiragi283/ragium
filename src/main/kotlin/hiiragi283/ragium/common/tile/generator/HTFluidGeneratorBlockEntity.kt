package hiiragi283.ragium.common.tile.generator

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidVariant
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import hiiragi283.ragium.api.util.HTMachineException
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Supplier

abstract class HTFluidGeneratorBlockEntity(type: Supplier<out BlockEntityType<*>>, pos: BlockPos, state: BlockState) :
    HTMachineBlockEntity(type, pos, state),
    HTItemSlotHandler.Empty {
    private val inputTank: HTFluidTank = HTFluidTank
        .Builder()
        .setValidator(this::isFluidValid)
        .setCallback(this::setChanged)
        .build("fluid_input")

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, registryOps)
        inputTank.writeNbt(nbt, registryOps)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        super.readNbt(nbt, registryOps)
        inputTank.readNbt(nbt, registryOps)
    }

    abstract fun isFluidValid(variant: HTFluidVariant): Boolean

    abstract fun getFuelAmount(variant: HTFluidVariant): Int

    override fun checkCondition(level: ServerLevel, pos: BlockPos, simulate: Boolean): Result<Unit> =
        checkEnergyGenerate(level, 1280, simulate)

    override fun process(level: ServerLevel, pos: BlockPos) {
        val resourceIn: HTFluidVariant = inputTank.resource
        var amount: Int = getFuelAmount(resourceIn)
        if (amount <= 0) throw HTMachineException.Custom("Required fuel amount is negative value!")

        if (!inputTank.canExtract(amount)) throw HTMachineException.ShrinkFluid()
        inputTank.extract(amount, false)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    override fun onUpdateEnchantment(newEnchantments: ItemEnchantments) {
        super.onUpdateEnchantment(newEnchantments)
        inputTank.onUpdateEnchantment(newEnchantments)
    }

    //    Fluid    //

    final override fun getFluidTank(tank: Int): HTFluidTank? = inputTank

    final override fun getFluidIoFromSlot(tank: Int): HTStorageIO = HTStorageIO.INPUT

    final override fun getTanks(): Int = 1
}
