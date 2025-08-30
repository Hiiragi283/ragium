package hiiragi283.ragium.api.storage.fluid

import net.minecraft.core.Direction
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * 向きに応じて制御された[IFluidHandler]の拡張インターフェース
 * @see [mekanism.api.fluid.ISidedFluidHandler]
 */
interface HTSidedFluidHandler : IFluidHandlerModifiable {
    fun getFluidSideFor(): Direction? = null

    fun setFluidInTank(tank: Int, stack: FluidStack, side: Direction?)

    override fun setFluidInTank(tank: Int, stack: FluidStack) {
        setFluidInTank(tank, stack, getFluidSideFor())
    }

    fun getTanks(side: Direction?): Int

    override fun getTanks(): Int = getTanks(getFluidSideFor())

    fun getFluidInTank(tank: Int, side: Direction?): FluidStack

    override fun getFluidInTank(tank: Int): FluidStack = getFluidInTank(tank, getFluidSideFor())

    fun getTankCapacity(tank: Int, side: Direction?): Int

    override fun getTankCapacity(tank: Int): Int = getTankCapacity(tank, getFluidSideFor())

    fun insertFluid(stack: FluidStack, simulate: Boolean, side: Direction?): FluidStack

    override fun insertFluid(stack: FluidStack, simulate: Boolean): FluidStack = insertFluid(stack, simulate, getFluidSideFor())

    fun extractFluid(stack: FluidStack, simulate: Boolean, side: Direction?): FluidStack

    override fun extractFluid(stack: FluidStack, simulate: Boolean): FluidStack = extractFluid(stack, simulate, getFluidSideFor())

    fun isFluidValid(tank: Int, stack: FluidStack, side: Direction?): Boolean

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = isFluidValid(tank, stack, getFluidSideFor())

    fun insertFluid(
        tank: Int,
        stack: FluidStack,
        simulate: Boolean,
        side: Direction?,
    ): FluidStack

    override fun insertFluid(tank: Int, stack: FluidStack, simulate: Boolean): FluidStack =
        insertFluid(tank, stack, simulate, getFluidSideFor())

    fun extractFluid(
        tank: Int,
        amount: Int,
        simulate: Boolean,
        side: Direction?,
    ): FluidStack

    override fun extractFluid(tank: Int, amount: Int, simulate: Boolean): FluidStack =
        extractFluid(tank, amount, simulate, getFluidSideFor())

    fun extractFluid(amount: Int, simulate: Boolean, side: Direction?): FluidStack

    override fun extractFluid(amount: Int, simulate: Boolean): FluidStack = extractFluid(amount, simulate, getFluidSideFor())
}
