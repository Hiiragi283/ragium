package hiiragi283.ragium.api.storage.fluid

import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * @see [mekanism.api.fluid.IExtendedFluidHandler]
 */
interface IFluidHandlerModifiable : IFluidHandler {
    fun setFluidInTank(tank: Int, stack: FluidStack)

    fun insertFluid(tank: Int, stack: FluidStack, simulate: Boolean): FluidStack

    fun extractFluid(tank: Int, amount: Int, simulate: Boolean): FluidStack

    fun insertFluid(stack: FluidStack, simulate: Boolean): FluidStack

    fun extractFluid(stack: FluidStack, simulate: Boolean): FluidStack

    fun extractFluid(amount: Int, simulate: Boolean): FluidStack

    @Deprecated("Use `insertFluid(Int, FluidStack, Boolean)` instead", level = DeprecationLevel.ERROR)
    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int =
        resource.amount - insertFluid(resource, action.simulate()).amount

    @Deprecated("Use `extractFluid(Int, FluidStack, Boolean)` instead", level = DeprecationLevel.ERROR)
    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack = extractFluid(resource, action.simulate())

    @Deprecated("Use `extractFluid(Int, FluidStack, Boolean)` instead", level = DeprecationLevel.ERROR)
    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack = extractFluid(maxDrain, action.simulate())
}
