package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import net.minecraft.core.Direction
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [HTFluidTank]に基づいた[HTSidedFluidHandler]の拡張インターフェース
 * @see [mekanism.api.fluid.IMekanismFluidHandler]
 */
interface HTFluidHandler :
    HTSidedFluidHandler,
    HTContentListener {
    fun hasFluidHandler(): Boolean = true

    fun getFluidTanks(side: Direction?): List<HTFluidTank>

    fun getFluidTank(tank: Int, side: Direction?): HTFluidTank? = getFluidTanks(side).getOrNull(tank)

    override fun getFluidInTank(tank: Int, side: Direction?): FluidStack = getFluidTank(tank, side)?.getStack() ?: FluidStack.EMPTY

    override fun getTanks(side: Direction?): Int = getFluidTanks(side).size

    override fun getTankCapacity(tank: Int, side: Direction?): Int = getFluidTank(tank, side)?.capacity ?: 0

    override fun isFluidValid(tank: Int, stack: FluidStack, side: Direction?): Boolean =
        getFluidTank(tank, side)?.isFluidValid(stack) ?: false

    override fun insertFluid(stack: FluidStack, simulate: Boolean, side: Direction?): FluidStack {
        val tanks: List<HTFluidTank> = getFluidTanks(side)
        val access: HTStorageAccess = HTStorageAccess.forHandler(side)
        return when (tanks.size) {
            1 -> tanks[0].insert(stack, simulate, access)
            2 -> {
                val first: FluidStack = tanks[0].insert(stack, simulate, access)
                tanks[1].insert(first, simulate, access)
            }
            else -> stack
        }
    }

    override fun extractFluid(amount: Int, simulate: Boolean, side: Direction?): FluidStack {
        val tanks: List<HTFluidTank> = getFluidTanks(side)
        val access: HTStorageAccess = HTStorageAccess.forHandler(side)
        return when (tanks.size) {
            1 -> tanks[0].extract(amount, simulate, access)
            2 -> {
                val first: FluidStack = tanks[0].extract(amount, simulate, access)
                val tank1: HTFluidTank = tanks[1]
                if (!FluidStack.isSameFluidSameComponents(first, tank1.getStack())) return first
                tank1.extract(first.amount, simulate, access)
            }
            else -> FluidStack.EMPTY
        }
    }

    override fun extractFluid(stack: FluidStack, simulate: Boolean, side: Direction?): FluidStack {
        val tanks: List<HTFluidTank> = getFluidTanks(side)
        val access: HTStorageAccess = HTStorageAccess.forHandler(side)
        return when (tanks.size) {
            1 -> {
                val tank: HTFluidTank = tanks[0]
                if (!FluidStack.isSameFluidSameComponents(stack, tank.getStack())) return FluidStack.EMPTY
                tank.extract(stack.amount, simulate, access)
            }
            2 -> {
                val tank: HTFluidTank = tanks[0]
                if (!FluidStack.isSameFluidSameComponents(stack, tank.getStack())) return FluidStack.EMPTY
                val first: FluidStack = tank.extract(stack.amount, simulate, access)

                val tank1: HTFluidTank = tanks[1]
                if (!FluidStack.isSameFluidSameComponents(first, tank1.getStack())) return first
                tank1.extract(first.amount, simulate, access)
            }
            else -> FluidStack.EMPTY
        }
    }
}
