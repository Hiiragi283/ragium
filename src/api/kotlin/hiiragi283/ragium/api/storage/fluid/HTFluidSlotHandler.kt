package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.storage.HTStorageIO
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * @see [mekanism.api.fluid.IMekanismFluidHandler]
 */
interface HTFluidSlotHandler : IFluidHandler {
    fun getFluidIoFromSlot(tank: Int): HTStorageIO

    fun getFluidTank(tank: Int): HTFluidTank?

    //    IFluidHandler    //

    override fun getFluidInTank(tank: Int): FluidStack = getFluidTank(tank)?.fluid ?: FluidStack.EMPTY

    override fun getTankCapacity(tank: Int): Int = getFluidTank(tank)?.capacity ?: 0

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = getFluidTank(tank)?.isFluidValid(stack) == true

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
        var filled = 0
        for (index: Int in (0 until tanks)) {
            if (!getFluidIoFromSlot(index).canInsert) continue
            val tank: HTFluidTank = getFluidTank(index) ?: continue
            filled += tank.fill(resource, action)
        }
        return filled
    }

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
        if (maxDrain <= 0) return FluidStack.EMPTY
        for (index: Int in (0 until tanks)) {
            if (!getFluidIoFromSlot(index).canExtract) continue
            val tank: HTFluidTank = getFluidTank(index) ?: continue
            val drained: FluidStack = tank.drain(maxDrain, action)
            if (!drained.isEmpty) return drained
        }
        return FluidStack.EMPTY
    }

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack = drain(resource.amount, action)

    //    Empty    //

    interface Empty : HTFluidSlotHandler {
        override fun getFluidIoFromSlot(tank: Int): HTStorageIO = HTStorageIO.EMPTY

        override fun getFluidTank(tank: Int): HTFluidTank? = null

        override fun getTanks(): Int = 0
    }
}
