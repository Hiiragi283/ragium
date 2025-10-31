package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.stack.toImmutable
import net.minecraft.core.Direction
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * [HTFluidTank]に基づいた[HTSidedFluidHandler]の拡張インターフェース
 * @see mekanism.api.fluid.IMekanismFluidHandler
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

    /**
     * @see blusunrize.immersiveengineering.common.fluids.ArrayFluidHandler.fill
     */
    @Suppress("DEPRECATION")
    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction, side: Direction?): Int {
        if (resource.isEmpty) return 0
        val remaining: FluidStack = resource.copy()
        var existing: HTFluidTank? = null
        for (tank: HTFluidTank in getFluidTanks(side)) {
            if (tank.isSameStack(remaining.toImmutable())) {
                existing = tank
                break
            }
        }
        if (existing != null) {
            remaining.shrink(existing.fill(remaining, action))
        } else {
            for (tank: HTFluidTank in getFluidTanks(side)) {
                val filled: Int = tank.fill(remaining, action)
                remaining.shrink(filled)
                if (filled > 0) {
                    break
                }
            }
        }
        return resource.amount - remaining.amount
    }

    /**
     * @see blusunrize.immersiveengineering.common.fluids.ArrayFluidHandler.drain
     */
    @Suppress("DEPRECATION")
    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction, side: Direction?): FluidStack {
        for (tank: HTFluidTank in getFluidTanks(side)) {
            val drained: FluidStack = tank.drain(resource, action)
            if (!drained.isEmpty) {
                return drained
            }
        }
        return FluidStack.EMPTY
    }

    /**
     * @see blusunrize.immersiveengineering.common.fluids.ArrayFluidHandler.drain
     */
    @Suppress("DEPRECATION")
    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction, side: Direction?): FluidStack {
        for (tank: HTFluidTank in getFluidTanks(side)) {
            val drained: FluidStack = tank.drain(maxDrain, action)
            if (!drained.isEmpty) {
                return drained
            }
        }
        return FluidStack.EMPTY
    }
}
