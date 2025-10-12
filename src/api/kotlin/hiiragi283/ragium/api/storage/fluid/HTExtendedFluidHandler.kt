package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.storage.HTStorageAction
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * @see [mekanism.api.fluid.IExtendedFluidHandler]
 */
interface HTExtendedFluidHandler : IFluidHandler {
    fun insertFluid(stack: FluidStack, action: HTStorageAction): FluidStack

    fun extractFluid(stack: FluidStack, action: HTStorageAction): FluidStack

    fun extractFluid(amount: Int, action: HTStorageAction): FluidStack

    @Deprecated("Use `insertFluid(Int, FluidStack, Boolean)` instead", level = DeprecationLevel.ERROR)
    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int =
        resource.amount - insertFluid(resource, HTStorageAction.of(action)).amount

    @Deprecated("Use `extractFluid(Int, FluidStack, Boolean)` instead", level = DeprecationLevel.ERROR)
    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack =
        extractFluid(resource, HTStorageAction.of(action))

    @Deprecated("Use `extractFluid(Int, FluidStack, Boolean)` instead", level = DeprecationLevel.ERROR)
    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack = extractFluid(maxDrain, HTStorageAction.of(action))
}
