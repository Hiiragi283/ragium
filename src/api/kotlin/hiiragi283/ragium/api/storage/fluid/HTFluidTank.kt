package hiiragi283.ragium.api.storage.fluid

import hiiragi283.ragium.api.storage.HTStackSlot
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.IFluidTank
import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * [ImmutableFluidStack]向けの[HTStackSlot]の拡張インターフェース
 */
interface HTFluidTank :
    HTStackSlot<ImmutableFluidStack>,
    IFluidTank {
    fun toSingleHandler(): IFluidHandler = HTFluidTankWrapper(this)

    //    IFluidTank    //

    @Deprecated("Use `getStack()` instead", ReplaceWith("this.getStack()"), DeprecationLevel.ERROR)
    override fun getFluid(): FluidStack = getFluidStack()

    @Deprecated("Use `getAmountAsInt()` instead", ReplaceWith("this.getAmountAsInt()"), DeprecationLevel.ERROR)
    override fun getFluidAmount(): Int = getAmountAsInt()

    @Deprecated("Use `getCapacityAsInt(FluidStack)` instead", level = DeprecationLevel.ERROR)
    override fun getCapacity(): Int = getCapacityAsInt(getStack())

    @Deprecated("Use `isValid(FluidStack)` instead", ReplaceWith("this.isValid(stack)"), DeprecationLevel.ERROR)
    override fun isFluidValid(stack: FluidStack): Boolean = isValid(stack)

    @Deprecated("Use `insert(FluidStack, Boolean, HTStorageAccess) `instead")
    override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int =
        resource.amount - insertFluid(resource, HTStorageAction.of(action), HTStorageAccess.EXTERNAL).amount

    @Deprecated("Use `extract(FluidStack, Boolean, HTStorageAccess)` instead")
    override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack =
        extractFluid(maxDrain, HTStorageAction.of(action), HTStorageAccess.EXTERNAL)

    @Deprecated("Use `extract(FluidStack, Boolean, HTStorageAccess)` instead")
    override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack {
        if (!FluidStack.isSameFluidSameComponents(resource, getFluidStack())) return FluidStack.EMPTY
        return extractFluid(resource.amount, HTStorageAction.of(action), HTStorageAccess.EXTERNAL)
    }

    //    Mutable    //

    /**
     * [ImmutableFluidStack]向けの[HTStackSlot.Mutable]の拡張クラス
     */
    abstract class Mutable :
        HTStackSlot.Mutable<ImmutableFluidStack>(),
        HTFluidTank {
        final override fun getEmptyStack(): ImmutableFluidStack = ImmutableFluidStack.EMPTY

        final override fun isSameStack(first: ImmutableFluidStack, second: ImmutableFluidStack): Boolean =
            FluidStack.isSameFluidSameComponents(first.stack, second.stack)
    }
}
