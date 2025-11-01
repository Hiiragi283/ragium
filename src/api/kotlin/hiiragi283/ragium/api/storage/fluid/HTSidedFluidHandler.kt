package hiiragi283.ragium.api.storage.fluid

import net.minecraft.core.Direction
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * 向きに応じて制御された[IFluidHandler]の拡張インターフェース
 * @see mekanism.api.fluid.ISidedFluidHandler
 */
interface HTSidedFluidHandler : IFluidHandler {
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

    fun isFluidValid(tank: Int, stack: FluidStack, side: Direction?): Boolean

    @Deprecated("Use `isFluidValid(Int, FluidStack, Direction?)` instead")
    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean = isFluidValid(tank, stack, getFluidSideFor())

    fun fill(resource: FluidStack, action: IFluidHandler.FluidAction, side: Direction?): Int

    @Deprecated("Use `fill(FluidStack, IFluidHandler.FluidAction, Direction?)` instead")
    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int = fill(resource, action, getFluidSideFor())

    fun drain(resource: FluidStack, action: IFluidHandler.FluidAction, side: Direction?): FluidStack

    @Deprecated("Use `drain(FluidStack, IFluidHandler.FluidAction, Direction?)` instead")
    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack = drain(resource, action, getFluidSideFor())

    fun drain(maxDrain: Int, action: IFluidHandler.FluidAction, side: Direction?): FluidStack

    @Deprecated("Use `drain(Int, IFluidHandler.FluidAction, Direction?)` instead")
    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack = drain(maxDrain, action, getFluidSideFor())
}
