package hiiragi283.ragium.api.fluid

import hiiragi283.ragium.api.util.HTStorageIO
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.IFluidTank
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import java.util.function.Supplier

class LimitedFluidTank(val storageIO: HTStorageIO, val delegate: Supplier<IFluidTank>) : IFluidTank {
    override fun getFluid(): FluidStack = delegate.get().fluid

    override fun getFluidAmount(): Int = delegate.get().fluidAmount

    override fun getCapacity(): Int = delegate.get().capacity

    override fun isFluidValid(stack: FluidStack): Boolean = delegate.get().isFluidValid(stack)

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
        if (!storageIO.canInsert) return 0
        return delegate.get().fill(resource, action)
    }

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
        if (!storageIO.canExtract) return FluidStack.EMPTY
        return delegate.get().drain(maxDrain, action)
    }

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        if (!storageIO.canExtract) return FluidStack.EMPTY
        return delegate.get().drain(resource, action)
    }
}
