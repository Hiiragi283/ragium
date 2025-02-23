package hiiragi283.ragium.common.capability.fluid

import hiiragi283.ragium.api.capability.fluid.HTMachineFluidTank
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class HTReadOnlyFluidHandler(val tanks: List<HTMachineFluidTank>) : IFluidHandler {
    constructor(vararg tanks: HTMachineFluidTank) : this(tanks.toList())

    override fun getTanks(): Int = tanks.size

    private fun getTank(index: Int): HTMachineFluidTank? = tanks.getOrNull(index)

    override fun getFluidInTank(tank: Int): FluidStack = getTank(tank)?.fluid ?: FluidStack.EMPTY

    override fun getTankCapacity(tank: Int): Int = getTank(tank)?.capacity ?: 0

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = getTank(tank)?.isFluidValid(stack) == true

    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int = 0

    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack = FluidStack.EMPTY

    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack = FluidStack.EMPTY
}
