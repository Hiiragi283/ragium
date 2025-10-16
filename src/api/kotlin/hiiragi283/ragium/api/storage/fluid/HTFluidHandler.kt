package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import net.minecraft.core.Direction
import net.neoforged.neoforge.fluids.FluidStack

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
        return tank.getCapacityAsInt(tank.getStack())
    }

    override fun isFluidValid(tank: Int, stack: FluidStack, side: Direction?): Boolean = getFluidTank(tank, side)?.isValid(stack) ?: false

    override fun insertFluid(stack: FluidStack, action: HTStorageAction, side: Direction?): FluidStack {
        val tanks: List<HTFluidTank> = getFluidTanks(side)
        val access: HTStorageAccess = HTStorageAccess.forHandler(side)
        return when (tanks.size) {
            1 -> tanks[0].insertFluid(stack, action, access)
            2 -> {
                val first: FluidStack = tanks[0].insertFluid(stack, action, access)
                tanks[1].insertFluid(first, action, access)
            }
            else -> stack
        }
    }

    override fun extractFluid(amount: Int, action: HTStorageAction, side: Direction?): FluidStack {
        val tanks: List<HTFluidTank> = getFluidTanks(side)
        val access: HTStorageAccess = HTStorageAccess.forHandler(side)
        return when (tanks.size) {
            1 -> tanks[0].extractFluid(amount, action, access)
            2 -> {
                val first: FluidStack = tanks[0].extractFluid(amount, action, access)
                val tank1: HTFluidTank = tanks[1]
                if (!FluidStack.isSameFluidSameComponents(first, tank1.getFluidStack())) return first
                tank1.extractFluid(first.amount, action, access)
            }
            else -> FluidStack.EMPTY
        }
    }

    override fun extractFluid(stack: FluidStack, action: HTStorageAction, side: Direction?): FluidStack {
        val tanks: List<HTFluidTank> = getFluidTanks(side)
        val access: HTStorageAccess = HTStorageAccess.forHandler(side)
        return when (tanks.size) {
            1 -> {
                val tank: HTFluidTank = tanks[0]
                if (!FluidStack.isSameFluidSameComponents(stack, tank.getFluidStack())) return FluidStack.EMPTY
                tank.extractFluid(stack.amount, action, access)
            }
            2 -> {
                val tank: HTFluidTank = tanks[0]
                if (!FluidStack.isSameFluidSameComponents(stack, tank.getFluidStack())) return FluidStack.EMPTY
                val first: FluidStack = tank.extractFluid(stack.amount, action, access)

                val tank1: HTFluidTank = tanks[1]
                if (!FluidStack.isSameFluidSameComponents(first, tank1.getFluidStack())) return first
                tank1.extractFluid(first.amount, action, access)
            }
            else -> FluidStack.EMPTY
        }
    }
}
