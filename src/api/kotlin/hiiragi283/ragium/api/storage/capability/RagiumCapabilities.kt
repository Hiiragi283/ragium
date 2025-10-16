package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.energy.HTEnergyHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable

object RagiumCapabilities {
    @JvmField
    val ITEM: HTMultiCapability<IItemHandler, IItemHandler, HTItemHandler, HTItemSlot> = HTMultiCapabilityBase(
        Capabilities.ItemHandler.BLOCK,
        Capabilities.ItemHandler.ITEM,
        ::wrapHandler,
        HTItemHandler::getItemSlots,
    )

    @JvmField
    val FLUID: HTMultiCapability<IFluidHandler, IFluidHandlerItem, HTFluidHandler, HTFluidTank> = HTMultiCapabilityBase(
        Capabilities.FluidHandler.BLOCK,
        Capabilities.FluidHandler.ITEM,
        ::wrapHandler,
        HTFluidHandler::getFluidTanks,
    )

    @JvmField
    val ENERGY: HTMultiCapability<IEnergyStorage, IEnergyStorage, HTEnergyHandler, HTEnergyBattery> = HTMultiCapabilityBase(
        Capabilities.EnergyStorage.BLOCK,
        Capabilities.EnergyStorage.ITEM,
        ::wrapStorage,
    ) { handler, side -> handler.getEnergyBattery(side).let(::listOfNotNull) }

    //    Wrapper    //

    // Item
    @JvmStatic
    private fun wrapHandler(handler: IItemHandler): HTItemHandler = handler as? HTItemHandler
        ?: HTItemHandler { (0..<handler.slots).map { index: Int -> createSlot(handler, index) } }

    @JvmStatic
    private fun createSlot(handler: IItemHandler, index: Int): HTItemSlot = object : HTItemSlot.Mutable(), HTValueSerializable.Empty {
        override fun getStack(): ImmutableItemStack = handler.getStackInSlot(index).toImmutable()

        override fun getCapacityAsLong(stack: ImmutableItemStack): Long = handler.getSlotLimit(index).toLong()

        override fun isValid(stack: ImmutableItemStack): Boolean = handler.isItemValid(index, stack.stack)

        override fun onContentsChanged() {}

        override fun setStack(stack: ImmutableItemStack) {
            (handler as? IItemHandlerModifiable)?.setStackInSlot(index, stack.stack)
        }
    }

    // Fluid
    @JvmStatic
    private fun wrapHandler(handler: IFluidHandler): HTFluidHandler = handler as? HTFluidHandler
        ?: HTFluidHandler { (0..<handler.tanks).map { index: Int -> createTank(handler, index) } }

    @JvmStatic
    private fun createTank(handler: IFluidHandler, index: Int): HTFluidTank = object : HTFluidTank, HTValueSerializable.Empty {
        override fun getStack(): ImmutableFluidStack = handler.getFluidInTank(index).toImmutable()

        override fun getCapacityAsLong(stack: ImmutableFluidStack): Long = handler.getTankCapacity(index).toLong()

        override fun isValid(stack: ImmutableFluidStack): Boolean = handler.isFluidValid(index, stack.stack)

        override fun insert(stack: ImmutableFluidStack, action: HTStorageAction, access: HTStorageAccess): ImmutableFluidStack =
            when (val filled: Int = handler.fill(stack.stack, action.toFluid())) {
                0 -> stack
                else -> stack.copyAndShrink(filled)
            }

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): ImmutableFluidStack =
            handler.drain(amount, action.toFluid()).toImmutable()

        override fun onContentsChanged() {}
    }

    // Energy
    @JvmStatic
    private fun wrapStorage(storage: IEnergyStorage): HTEnergyHandler = storage as? HTEnergyHandler ?: HTEnergyHandler {
        object : HTEnergyBattery, HTValueSerializable.Empty {
            override fun getAmountAsLong(): Long = storage.energyStored.toLong()

            override fun getCapacityAsLong(): Long = storage.maxEnergyStored.toLong()

            override fun insertEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int =
                storage.receiveEnergy(amount, action.simulate)

            override fun extractEnergy(amount: Int, action: HTStorageAction, access: HTStorageAccess): Int =
                storage.extractEnergy(amount, action.simulate)

            override fun onContentsChanged() {}
        }
    }
}
