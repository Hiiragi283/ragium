package hiiragi283.ragium.api.capability

import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import java.util.function.IntFunction

class LimitedFluidHandler(private val ioProvider: IntFunction<HTStorageIO>, private val delegate: Map<Int, IFluidHandler>) :
    IFluidHandler {
    override fun getTanks(): Int = delegate.values.sumOf(IFluidHandler::getTanks)

    override fun getFluidInTank(tank: Int): FluidStack = delegate[tank]?.getFluidInTank(tank) ?: FluidStack.EMPTY

    override fun getTankCapacity(tank: Int): Int = delegate[tank]?.getTankCapacity(tank) ?: 0

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = delegate[tank]?.isFluidValid(tank, stack) == true

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
        for ((index: Int, tank: IFluidHandler) in delegate.entries) {
            if (!ioProvider.apply(index).canInsert) continue
            return tank.fill(resource, action)
        }
        return 0
    }

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        for ((index: Int, tank: IFluidHandler) in delegate.entries) {
            if (!ioProvider.apply(index).canExtract) continue
            return tank.drain(resource, action)
        }
        return FluidStack.EMPTY
    }

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
        for ((index: Int, tank: IFluidHandler) in delegate.entries) {
            if (!ioProvider.apply(index).canExtract) continue
            return tank.drain(maxDrain, action)
        }
        return FluidStack.EMPTY
    }
}
