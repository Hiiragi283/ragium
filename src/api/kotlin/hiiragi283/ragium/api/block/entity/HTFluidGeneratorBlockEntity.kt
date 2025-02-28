package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.event.HTGeneratorFuelTimeEvent
import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.storage.HTFluidTank
import hiiragi283.ragium.api.storage.HTItemSlotHandler
import hiiragi283.ragium.api.storage.HTStorageIO
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
import net.neoforged.neoforge.fluids.FluidStack
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import java.util.function.Supplier

abstract class HTFluidGeneratorBlockEntity(
    type: Supplier<out BlockEntityType<*>>,
    pos: BlockPos,
    state: BlockState,
    machineType: HTMachineType,
) : HTMachineBlockEntity(type, pos, state, machineType),
    HTItemSlotHandler.Empty {
    private val inputTank: HTFluidTank = HTFluidTank
        .Builder()
        .setValidator(this::isFluidValid)
        .setCallback(this::setChanged)
        .build("fluid_input")

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.writeNbt(nbt, dynamicOps)
        inputTank.writeNbt(nbt, dynamicOps)
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        super.readNbt(nbt, dynamicOps)
        inputTank.readNbt(nbt, dynamicOps)
    }

    abstract fun isFluidValid(stack: FluidStack): Boolean

    abstract fun getFuelAmount(stack: FluidStack): Int

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Generate.CHEMICAL

    override fun process(level: ServerLevel, pos: BlockPos) {
        val stackIn: FluidStack = inputTank.fluid
        var amount: Int = getFuelAmount(stackIn)
        if (amount <= 0) {
            val event = HTGeneratorFuelTimeEvent(this, stackIn, amount)
            FORGE_BUS.post(event)
            if (event.isCanceled) throw HTMachineException.Custom("HTGeneratorFuelTimeEvent was canceled!")
            amount = event.fuelTime
        }
        if (amount <= 0) throw HTMachineException.Custom("Required fuel amount is negative value!")

        if (!inputTank.canShrink(amount, true)) throw HTMachineException.ShrinkFluid()
        inputTank.shrinkStack(amount, false)
    }

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        super.updateEnchantments(newEnchantments)
        inputTank.updateCapacity(this)
    }

    //    Fluid    //

    final override fun getFluidTank(tank: Int): HTFluidTank? = inputTank

    final override fun getFluidIoFromSlot(tank: Int): HTStorageIO = HTStorageIO.INPUT

    final override fun getTanks(): Int = 1
}
