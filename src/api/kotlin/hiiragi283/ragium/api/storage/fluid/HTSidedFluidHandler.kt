package hiiragi283.ragium.api.storage.fluid

import net.minecraft.core.Direction
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 向きに応じて制御された[HTExtendedFluidHandler]の拡張インターフェース
 * @see [mekanism.api.fluid.ISidedFluidHandler]
 */
interface HTSidedFluidHandler : HTExtendedFluidHandler {
    fun getFluidSideFor(): Direction? = null

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

    fun extractFluid(amount: Int, simulate: Boolean, side: Direction?): FluidStack

    override fun extractFluid(amount: Int, simulate: Boolean): FluidStack = extractFluid(amount, simulate, getFluidSideFor())
}
