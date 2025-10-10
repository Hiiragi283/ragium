package hiiragi283.ragium.api.storage.fluid

import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

@Suppress("DEPRECATION")
class HTFluidTankWrapper(private val tank: HTFluidTank) : IFluidHandler {
    override fun getTanks(): Int = 1

    override fun getFluidInTank(tank: Int): FluidStack = this.tank.getFluidStack()

    override fun getTankCapacity(tank: Int): Int = this.tank.getCapacityAsInt(getFluidInTank(0))

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = this.tank.isValid(stack)

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int = this.tank.fill(resource, action)

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack = this.tank.drain(resource, action)

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack = this.tank.drain(maxDrain, action)
}
