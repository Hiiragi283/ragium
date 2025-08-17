package hiiragi283.ragium.api.storage.fluid

import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.IFluidTank
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class HTFilteredFluidHandler(private val delegate: List<HTFluidTank>, private val filter: HTFluidFilter) : IFluidHandlerModifiable {
    constructor(tank: HTFluidTank, filter: HTFluidFilter) : this(listOf(tank), filter)

    constructor(tankIn: HTFluidTank, tankOut: HTFluidTank) : this(
        listOf(tankIn, tankOut),
        object : HTFluidFilter {
            override fun canFill(tank: IFluidTank, stack: FluidStack): Boolean = tank == tankIn

            override fun canDrain(tank: IFluidTank, stack: FluidStack): Boolean = tank == tankOut

            override fun canDrain(tank: IFluidTank, maxDrain: Int): Boolean = tank == tankOut
        },
    )

    override fun setFluidInTank(tank: Int, stack: FluidStack) {
        delegate[tank].fluid = stack
    }

    override fun getTanks(): Int = delegate.size

    override fun getFluidInTank(tank: Int): FluidStack = delegate[tank].fluid

    override fun getTankCapacity(tank: Int): Int = delegate[tank].capacity

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = delegate[tank].isFluidValid(stack)

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
        for (tank: HTFluidTank in delegate) {
            if (!filter.canFill(tank, resource)) continue
            val filled: Int = tank.fill(resource, action)
            if (filled > 0) return filled
        }
        return 0
    }

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        for (tank: HTFluidTank in delegate) {
            if (!filter.canDrain(tank, resource)) continue
            val drained: FluidStack = tank.drain(resource, action)
            if (!drained.isEmpty) return drained
        }
        return FluidStack.EMPTY
    }

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
        for (tank: HTFluidTank in delegate) {
            if (!filter.canDrain(tank, maxDrain)) continue
            val drained: FluidStack = tank.drain(maxDrain, action)
            if (!drained.isEmpty) return drained
        }
        return FluidStack.EMPTY
    }
}
