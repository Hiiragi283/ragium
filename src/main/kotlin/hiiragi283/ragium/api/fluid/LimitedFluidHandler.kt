package hiiragi283.ragium.api.fluid

import hiiragi283.ragium.api.util.HTStorageIO
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import java.util.function.IntFunction
import java.util.function.Supplier

class LimitedFluidHandler(private val ioProvider: IntFunction<HTStorageIO>, private val delegate: Supplier<IFluidHandler>) :
    IFluidHandler {
    override fun getTanks(): Int = delegate.get().tanks

    override fun getFluidInTank(tank: Int): FluidStack = delegate.get().getFluidInTank(tank)

    override fun getTankCapacity(tank: Int): Int = delegate.get().getTankCapacity(tank)

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = delegate.get().isFluidValid(tank, stack)

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int = delegate.get().fill(resource, action)

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack = delegate.get().drain(resource, action)

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack = delegate.get().drain(maxDrain, action)
}
