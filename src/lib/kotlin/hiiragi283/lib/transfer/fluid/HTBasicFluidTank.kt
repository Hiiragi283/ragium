package hiiragi283.lib.transfer.fluid

import hiiragi283.lib.HTConstants
import hiiragi283.lib.transfer.HTStackResourceSlot
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.HTTransferPredicates
import hiiragi283.lib.transfer.HTTransferValidators
import java.util.function.BiPredicate
import java.util.function.Predicate
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource

open class HTBasicFluidTank(
    capacity: Int,
    canExtract: BiPredicate<FluidResource, HTTransferAccess>,
    canInsert: BiPredicate<FluidResource, HTTransferAccess>,
    filter: Predicate<FluidResource>,
    listener: Runnable?,
) : HTStackResourceSlot.Basic<FluidStack, FluidResource>(capacity, canExtract, canInsert, filter, listener) {
    companion object {
        @JvmStatic
        fun create(
            capacity: Int,
            listener: Runnable?,
            canInsert: BiPredicate<FluidResource, HTTransferAccess> = HTTransferPredicates.alwaysTrueBi(),
            canExtract: BiPredicate<FluidResource, HTTransferAccess> = HTTransferPredicates.alwaysTrueBi(),
            filter: Predicate<FluidResource> = HTTransferPredicates.alwaysTrue(),
        ): HTBasicFluidTank = HTBasicFluidTank(HTTransferValidators.validateCapacity(capacity), canInsert, canExtract, filter, listener)

        @JvmStatic
        fun input(
            capacity: Int,
            listener: Runnable?,
            canInsert: Predicate<FluidResource> = HTTransferPredicates.alwaysTrue(),
            filter: Predicate<FluidResource> = canInsert,
        ): HTBasicFluidTank = create(capacity, listener, canInsert = { resource, _ -> canInsert.test(resource) }, canExtract = HTTransferPredicates.notExternal(), filter = filter)

        @JvmStatic
        fun output(capacity: Int, listener: Runnable?): HTBasicFluidTank = create(capacity, listener, canInsert = HTTransferPredicates.internalOnly())
    }

    private var stackIn: FluidStack = FluidStack.EMPTY

    override fun getStackCopy(): FluidStack = stackIn.copy()

    override fun setStack(stack: FluidStack) {
        setStackUnchecked(stack, true)
    }

    override fun setStackInternal(stack: FluidStack) {
        setStackUnchecked(stack, false)
    }

    private fun setStackUnchecked(other: FluidStack, validate: Boolean) {
        val resource: FluidResource = getResourceFrom(other)
        if (resource.isEmpty) {
            if (this.stackIn.isEmpty) return
            this.stackIn = FluidStack.EMPTY
        } else if (!validate || isValid(resource)) {
            this.stackIn = other
        } else {
            error("Invalid stack for tank: $other")
        }
        onRootCommit(getStackCopy())
    }

    final override fun getResourceFrom(stack: FluidStack): FluidResource = FluidResource.of(stack)

    final override fun getAmountFrom(stack: FluidStack): Int = stack.amount()

    final override fun isSame(stack: FluidStack, resource: FluidResource): Boolean = resource.matches(stack)

    final override fun createStack(resource: FluidResource, amount: Int): FluidStack = resource.toStack(amount)

    //    ValueIOSerializable    //

    override fun serialize(output: ValueOutput) {
        output.store(HTConstants.FLUID, FluidStack.OPTIONAL_CODEC, getStackCopy())
    }

    override fun deserialize(input: ValueInput) {
        input.read(HTConstants.FLUID, FluidStack.OPTIONAL_CODEC).ifPresent(::setStackInternal)
    }
}
