package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.storage.HTStorageAction
import net.minecraft.core.Direction
import net.neoforged.neoforge.fluids.FluidStack

/**
 * 向きに応じて制御された[HTExtendedFluidHandler]の拡張インターフェース
 * @see [mekanism.api.fluid.ISidedFluidHandler]
 */
interface HTSidedFluidHandler : HTExtendedFluidHandler {
    fun getFluidSideFor(): Direction? = null

    fun getTanks(side: Direction?): Int

    @Deprecated("Use `getTanks(Direction?)` instead")
    override fun getTanks(): Int = getTanks(getFluidSideFor())

    fun getFluidInTank(tank: Int, side: Direction?): FluidStack

    @Deprecated("Use `getFluidInTank(Int, Direction?)` instead")
    override fun getFluidInTank(tank: Int): FluidStack = getFluidInTank(tank, getFluidSideFor())

    fun getTankCapacity(tank: Int, side: Direction?): Int

    @Deprecated("Use `getTankCapacity(Int, Direction?)` instead")
    override fun getTankCapacity(tank: Int): Int = getTankCapacity(tank, getFluidSideFor())

    fun insertFluid(stack: FluidStack, action: HTStorageAction, side: Direction?): FluidStack

    @Deprecated("Use `insertFluid(FluidStack, Boolean, Direction?)` instead")
    override fun insertFluid(stack: FluidStack, action: HTStorageAction): FluidStack = insertFluid(stack, action, getFluidSideFor())

    fun extractFluid(stack: FluidStack, action: HTStorageAction, side: Direction?): FluidStack

    @Deprecated("Use `extractFluid(FluidStack, Boolean, Direction?)` instead")
    override fun extractFluid(stack: FluidStack, action: HTStorageAction): FluidStack = extractFluid(stack, action, getFluidSideFor())

    fun isFluidValid(tank: Int, stack: FluidStack, side: Direction?): Boolean

    @Deprecated("Use `isFluidValid(Int, FluidStack, Direction?)` instead")
    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = isFluidValid(tank, stack, getFluidSideFor())

    fun extractFluid(amount: Int, action: HTStorageAction, side: Direction?): FluidStack

    @Deprecated("Use `extractFluid(Int, Boolean, Direction?)` instead")
    override fun extractFluid(amount: Int, action: HTStorageAction): FluidStack = extractFluid(amount, action, getFluidSideFor())
}
