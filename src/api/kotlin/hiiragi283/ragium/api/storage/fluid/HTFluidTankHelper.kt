package hiiragi283.ragium.api.storage.fluid

import net.neoforged.neoforge.fluids.FluidStack

object HTFluidTankHelper {
    @JvmStatic
    fun canInsertFluid(tank: HTFluidTank, stack: FluidStack): Boolean {
        val result: FluidStack = stack.copy()
        val inserted: Int = tank.insert(result, true)
        if (inserted > 0) result.amount -= inserted
        return result.isEmpty
    }

    @JvmStatic
    fun insertFluid(tank: HTFluidTank, stack: FluidStack, simulate: Boolean): FluidStack {
        tank.insert(stack, simulate)
        return if (stack.isEmpty) FluidStack.EMPTY else stack
    }
}
