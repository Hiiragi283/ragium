package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
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

    override fun getTankCapacity(tank: Int, side: Direction?): Int {
        val tank: HTFluidTank = getFluidTank(tank, side) ?: return 0
        return tank.getCapacityAsInt(tank.getStack())
    }

    override fun isFluidValid(tank: Int, stack: FluidStack, side: Direction?): Boolean = getFluidTank(tank, side)?.isValid(stack) ?: false

    override fun insertFluid(stack: FluidStack, action: HTStorageAction, side: Direction?): FluidStack {
        val tanks: List<HTFluidTank> = getFluidTanks(side)
        val access: HTStorageAccess = HTStorageAccess.forHandler(side)
        return when (tanks.size) {
            1 -> tanks[0].insert(stack, action, access)
            2 -> {
                val first: FluidStack = tanks[0].insert(stack, action, access)
                tanks[1].insert(first, action, access)
            }
            else -> stack
        }
    }

    override fun extractFluid(amount: Int, action: HTStorageAction, side: Direction?): FluidStack {
        val tanks: List<HTFluidTank> = getFluidTanks(side)
        val access: HTStorageAccess = HTStorageAccess.forHandler(side)
        return when (tanks.size) {
            1 -> tanks[0].extract(amount, action, access)
            2 -> {
                val first: FluidStack = tanks[0].extract(amount, action, access)
                val tank1: HTFluidTank = tanks[1]
                if (!FluidStack.isSameFluidSameComponents(first, tank1.getStack())) return first
                tank1.extract(first.amount, action, access)
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
                if (!FluidStack.isSameFluidSameComponents(stack, tank.getStack())) return FluidStack.EMPTY
                tank.extract(stack.amount, action, access)
            }
            2 -> {
                val tank: HTFluidTank = tanks[0]
                if (!FluidStack.isSameFluidSameComponents(stack, tank.getStack())) return FluidStack.EMPTY
                val first: FluidStack = tank.extract(stack.amount, action, access)

                val tank1: HTFluidTank = tanks[1]
                if (!FluidStack.isSameFluidSameComponents(first, tank1.getStack())) return first
                tank1.extract(first.amount, action, access)
            }
            else -> FluidStack.EMPTY
        }
    }
}
