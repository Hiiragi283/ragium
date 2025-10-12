package hiiragi283.ragium.api.storage.capability

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.energy.HTEnergyHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.ImmutableFluidStack
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.ImmutableItemStack
import net.minecraft.core.Direction
import net.minecraft.world.inventory.Slot
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
        ?: object : HTItemHandler {
            override fun getItemSlots(side: Direction?): List<HTItemSlot> =
                (0..<handler.slots).mapNotNull { index: Int -> createSlot(handler, index) }

            override fun onContentsChanged() {}
        }

    @JvmStatic
    private fun createSlot(handler: IItemHandler, index: Int): HTItemSlot? = if (handler is HTItemHandler) {
        handler.getItemSlot(index, handler.getItemSideFor())
    } else {
        object : HTItemSlot.Mutable(), HTValueSerializable.Empty {
            override fun createContainerSlot(): Slot? = null

            override fun getStack(): ImmutableItemStack = ImmutableItemStack.of(handler.getStackInSlot(index))

            override fun getCapacityAsLong(stack: ImmutableItemStack): Long = handler.getSlotLimit(index).toLong()

            override fun isValid(stack: ImmutableItemStack): Boolean = handler.isItemValid(index, stack.stack)

            override fun onContentsChanged() {}

            override fun setStack(stack: ImmutableItemStack) {
                (handler as? IItemHandlerModifiable)?.setStackInSlot(index, stack.stack)
            }
        }
    }

    // Fluid
    @JvmStatic
    private fun wrapHandler(handler: IFluidHandler): HTFluidHandler = handler as? HTFluidHandler
        ?: object : HTFluidHandler {
            override fun getFluidTanks(side: Direction?): List<HTFluidTank> =
                (0..<handler.tanks).mapNotNull { index -> createTank(handler, index) }

            override fun onContentsChanged() {}
        }

    @JvmStatic
    private fun createTank(handler: IFluidHandler, index: Int): HTFluidTank? = if (handler is HTFluidHandler) {
        handler.getFluidTank(index, handler.getFluidSideFor())
    } else {
        object : HTFluidTank, HTValueSerializable.Empty {
            override fun getStack(): ImmutableFluidStack = ImmutableFluidStack.of(handler.getFluidInTank(index))

            override fun getCapacityAsLong(stack: ImmutableFluidStack): Long = handler.getTankCapacity(index).toLong()

            override fun isValid(stack: ImmutableFluidStack): Boolean = handler.isFluidValid(index, stack.stack)

            override fun insert(stack: ImmutableFluidStack, action: HTStorageAction, access: HTStorageAccess): ImmutableFluidStack =
                when (val filled: Int = handler.fill(stack.stack, action.toFluid())) {
                    0 -> ImmutableFluidStack.EMPTY
                    else -> stack.copyWithAmount(filled)
                }

            override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): ImmutableFluidStack =
                ImmutableFluidStack.of(handler.drain(amount, action.toFluid()))

            override fun onContentsChanged() {}
        }
    }

    // Energy
    @JvmStatic
    private fun wrapStorage(storage: IEnergyStorage): HTEnergyHandler =
        storage as? HTEnergyHandler ?: object : HTEnergyHandler, HTValueSerializable.Empty {
            override fun getEnergyBattery(side: Direction?): HTEnergyBattery? = createBattery(storage)

            override fun onContentsChanged() {}
        }

    @JvmStatic
    private fun createBattery(storage: IEnergyStorage): HTEnergyBattery? = if (storage is HTEnergyHandler) {
        storage.getEnergyBattery(storage.getEnergySideFor())
    } else {
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
