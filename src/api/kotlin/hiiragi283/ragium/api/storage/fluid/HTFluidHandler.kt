package hiiragi283.ragium.api.storage.fluid

import net.minecraft.core.Direction
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * [HTFluidTank]に基づいた[HTSidedFluidHandler]の拡張インターフェース
 * @see [mekanism.api.fluid.IMekanismFluidHandler]
 */
fun interface HTFluidHandler : HTSidedFluidHandler {
    fun hasFluidHandler(): Boolean = true

    fun getFluidTanks(side: Direction?): List<HTFluidTank>

    fun getFluidTank(tank: Int, side: Direction?): HTFluidTank? = getFluidTanks(side).getOrNull(tank)

    override fun getFluidInTank(tank: Int, side: Direction?): FluidStack = getFluidTank(tank, side)?.getFluidStack() ?: FluidStack.EMPTY

    override fun getTanks(side: Direction?): Int = getFluidTanks(side).size

    override fun getTankCapacity(tank: Int, side: Direction?): Int {
        val tank: HTFluidTank = getFluidTank(tank, side) ?: return 0
        return tank.getCapacity(tank.getStack())
    }

    override fun isFluidValid(tank: Int, stack: FluidStack, side: Direction?): Boolean = getFluidTank(tank, side)?.isValid(stack) ?: false

    @Suppress("DEPRECATION")
    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction, side: Direction?): Int {
        val tanks: List<HTFluidTank> = getFluidTanks(side)
        return when (tanks.size) {
            1 -> tanks[0].fill(resource, action)
            2 -> {
                val resource1: FluidStack = when {
                    action.simulate() -> resource.copy()
                    else -> resource
                }
                val first: Int = tanks[0].fill(resource1, action)
                tanks[1].fill(resource1.copyWithAmount(resource1.amount - first), action)
            }
            else -> 0
        }
    }

    @Suppress("DEPRECATION")
    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction, side: Direction?): FluidStack {
        val tanks: List<HTFluidTank> = getFluidTanks(side)
        return when (tanks.size) {
            1 -> tanks[0].drain(resource, action)
            2 -> {
                val first: FluidStack = tanks[0].drain(resource, action)
                tanks[1].drain(first, action)
            }
            else -> FluidStack.EMPTY
        }
    }

    @Suppress("DEPRECATION")
    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction, side: Direction?): FluidStack {
        val tanks: List<HTFluidTank> = getFluidTanks(side)
        return when (tanks.size) {
            1 -> tanks[0].drain(maxDrain, action)
            2 -> {
                val first: FluidStack = tanks[0].drain(maxDrain, action)
                tanks[1].drain(first, action)
            }
            else -> FluidStack.EMPTY
        }
    }
}
