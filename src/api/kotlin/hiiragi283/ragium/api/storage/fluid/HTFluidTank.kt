package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.HTStackSlot
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.IFluidTank
import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * [ImmutableFluidStack]向けの[HTStackSlot]の拡張インターフェース
 * @see mekanism.api.fluid.IExtendedFluidTank
 */
interface HTFluidTank :
    HTStackSlot<ImmutableFluidStack>,
    IFluidTank {
    override fun isSameStack(other: ImmutableFluidStack?): Boolean = FluidStack.isSameFluidSameComponents(
        this.getFluidStack(),
        other?.unwrap() ?: FluidStack.EMPTY,
    )

    //    IFluidTank    //

    @Deprecated("Use `getStack()` instead", ReplaceWith("this.getStack()"), DeprecationLevel.ERROR)
    override fun getFluid(): FluidStack = getFluidStack()

    @Deprecated("Use `getAmount()` instead", ReplaceWith("this.getAmount()"), DeprecationLevel.ERROR)
    override fun getFluidAmount(): Int = getAmount()

    override fun getCapacity(): Int = super.getCapacity()

    @Deprecated("Use `isValid(FluidStack)` instead", ReplaceWith("this.isValid(stack)"), DeprecationLevel.ERROR)
    override fun isFluidValid(stack: FluidStack): Boolean = isValid(stack)

    @Deprecated("Use `insert(FluidStack, Boolean, HTStorageAccess) `instead")
    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int =
        resource.amount - insertFluid(resource, HTStorageAction.of(action), HTStorageAccess.EXTERNAL).amount

    @Deprecated("Use `extract(FluidStack, Boolean, HTStorageAccess)` instead")
    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack =
        extractFluid(maxDrain, HTStorageAction.of(action), HTStorageAccess.EXTERNAL)

    @Deprecated("Use `extract(FluidStack, Boolean, HTStorageAccess)` instead")
    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack =
        extractFluid(resource, HTStorageAction.of(action), HTStorageAccess.EXTERNAL)

    //    Basic    //

    /**
     * [ImmutableFluidStack]向けの[HTStackSlot.Basic]の拡張クラス
     */
    abstract class Basic :
        HTStackSlot.Basic<ImmutableFluidStack>(),
        HTFluidTank
}
