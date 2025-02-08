package hiiragi283.ragium.common.fluid

import hiiragi283.ragium.api.capability.HTStorageIO
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class HTLimitedFluidHandler(val storageIO: HTStorageIO, val delegated: IFluidHandler) : IFluidHandler {
    override fun getTanks(): Int = delegated.tanks

    override fun getFluidInTank(tank: Int): FluidStack = delegated.getFluidInTank(tank)

    override fun getTankCapacity(tank: Int): Int = delegated.getTankCapacity(tank)

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = delegated.isFluidValid(tank, stack)

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
        if (!storageIO.canInsert) return 0
        return delegated.fill(resource, action)
    }

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        if (!storageIO.canExtract) return FluidStack.EMPTY
        return delegated.drain(resource, action)
    }

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
        if (!storageIO.canExtract) return FluidStack.EMPTY
        return delegated.drain(maxDrain, action)
    }
}
