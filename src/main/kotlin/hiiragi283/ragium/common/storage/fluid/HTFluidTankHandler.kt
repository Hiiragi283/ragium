package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.api.storage.HTStorageIO
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.IFluidTank
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class HTFluidTankHandler(val storageIO: HTStorageIO, val fluidTank: IFluidTank) : IFluidHandler {
    override fun getTanks(): Int = 1

    override fun getFluidInTank(tank: Int): FluidStack = fluidTank.fluid

    override fun getTankCapacity(tank: Int): Int = fluidTank.capacity

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = fluidTank.isFluidValid(stack)

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int {
        if (!storageIO.canInsert) return 0
        return fluidTank.fill(resource, action)
    }

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        if (!storageIO.canExtract) return FluidStack.EMPTY
        return fluidTank.drain(resource, action)
    }

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack {
        if (!storageIO.canExtract) return FluidStack.EMPTY
        return fluidTank.drain(maxDrain, action)
    }
}
