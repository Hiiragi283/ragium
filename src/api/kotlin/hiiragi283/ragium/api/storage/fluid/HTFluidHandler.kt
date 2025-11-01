package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
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
        return tank.getCapacity()
    }

    override fun isFluidValid(tank: Int, stack: FluidStack, side: Direction?): Boolean = getFluidTank(tank, side)?.isValid(stack) ?: false

    /**
     * @see blusunrize.immersiveengineering.common.fluids.ArrayFluidHandler.fill
     */
    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction, side: Direction?): Int {
        if (resource.isEmpty) return 0
        val action1: HTStorageAction = HTStorageAction.of(action)
        val access: HTStorageAccess = HTStorageAccess.forHandler(side)
        val remaining: FluidStack = resource.copy()
        var existing: HTFluidTank? = null
        for (tank: HTFluidTank in getFluidTanks(side)) {
            if (tank.isSameStack(remaining.toImmutable())) {
                existing = tank
                break
            }
        }
        if (existing != null) {
            val remainder: Int = existing.insert(remaining.toImmutable(), action1, access)?.amount() ?: 0
            remaining.amount = remainder
        } else {
            for (tank: HTFluidTank in getFluidTanks(side)) {
                val remainder: Int = tank.insert(remaining.toImmutable(), action1, access)?.amount() ?: 0
                if (remainder < remaining.amount) {
                    remaining.amount = remainder
                    break
                }
            }
        }
        return resource.amount - remaining.amount
    }

    /**
     * @see blusunrize.immersiveengineering.common.fluids.ArrayFluidHandler.drain
     */
    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction, side: Direction?): FluidStack {
        val action1: HTStorageAction = HTStorageAction.of(action)
        val access: HTStorageAccess = HTStorageAccess.forHandler(side)
        for (tank: HTFluidTank in getFluidTanks(side)) {
            val drained: ImmutableFluidStack? = tank.extract(resource.toImmutable(), action1, access)
            if (drained != null) {
                return drained.unwrap()
            }
        }
        return FluidStack.EMPTY
    }

    /**
     * @see blusunrize.immersiveengineering.common.fluids.ArrayFluidHandler.drain
     */
    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction, side: Direction?): FluidStack {
        val action1: HTStorageAction = HTStorageAction.of(action)
        val access: HTStorageAccess = HTStorageAccess.forHandler(side)
        for (tank: HTFluidTank in getFluidTanks(side)) {
            val drained: ImmutableFluidStack? = tank.extract(maxDrain, action1, access)
            if (drained != null) {
                return drained.unwrap()
            }
        }
        return FluidStack.EMPTY
    }
}
