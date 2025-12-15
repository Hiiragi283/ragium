package hiiragi283.ragium.common.storage.fluid.tank

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.function.HTPredicates
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.util.HTContentListener
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @see mekanism.common.capabilities.fluid.BasicFluidTank
 */
open class HTBasicFluidTank protected constructor(
    private val capacity: Int,
    private val canExtract: BiPredicate<ImmutableFluidStack, HTStorageAccess>,
    private val canInsert: BiPredicate<ImmutableFluidStack, HTStorageAccess>,
    private val filter: Predicate<ImmutableFluidStack>,
    private val listener: HTContentListener?,
) : HTFluidTank.Basic() {
    companion object {
        @JvmStatic
        private fun validateCapacity(capacity: Int): Int {
            check(capacity >= 0) { "Capacity must be non negative" }
            return capacity
        }

        @JvmStatic
        fun create(
            listener: HTContentListener?,
            capacity: Int,
            canExtract: BiPredicate<ImmutableFluidStack, HTStorageAccess> = HTPredicates.alwaysTrueBi(),
            canInsert: BiPredicate<ImmutableFluidStack, HTStorageAccess> = HTPredicates.alwaysTrueBi(),
            filter: Predicate<ImmutableFluidStack> = HTPredicates.alwaysTrue(),
        ): HTBasicFluidTank = HTBasicFluidTank(
            validateCapacity(capacity),
            canExtract,
            canInsert,
            filter,
            listener,
        )

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            capacity: Int,
            canInsert: Predicate<ImmutableFluidStack> = HTPredicates.alwaysTrue(),
            filter: Predicate<ImmutableFluidStack> = canInsert,
        ): HTBasicFluidTank = HTBasicFluidTank(
            validateCapacity(capacity),
            HTPredicates.notExternal(),
            { stack: ImmutableFluidStack, _ -> canInsert.test(stack) },
            filter,
            listener,
        )

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: Int): HTBasicFluidTank = HTBasicFluidTank(
            validateCapacity(capacity),
            HTPredicates.alwaysTrueBi(),
            HTPredicates.internalOnly(),
            HTPredicates.alwaysTrue(),
            listener,
        )
    }

    @JvmField
    protected var stack: FluidStack = FluidStack.EMPTY

    override fun getStack(): ImmutableFluidStack? = this.stack.toImmutable()

    override fun getCapacity(stack: ImmutableFluidStack?): Int = this.capacity

    final override fun isValid(stack: ImmutableFluidStack): Boolean = this.filter.test(stack)

    final override fun isStackValidForInsert(stack: ImmutableFluidStack, access: HTStorageAccess): Boolean =
        super.isStackValidForInsert(stack, access) && this.canInsert.test(stack, access)

    final override fun canStackExtract(stack: ImmutableFluidStack, access: HTStorageAccess): Boolean =
        super.canStackExtract(stack, access) && this.canExtract.test(stack, access)

    override fun serialize(output: HTValueOutput) {
        output.store(RagiumConst.FLUID, ImmutableFluidStack.CODEC, getStack())
    }

    override fun deserialize(input: HTValueInput) {
        input.readAndSet(RagiumConst.FLUID, ImmutableFluidStack.CODEC, ::setStackUnchecked)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }

    override fun setStack(stack: ImmutableFluidStack?) {
        setStackUnchecked(stack, true)
    }

    fun setStackUnchecked(stack: ImmutableFluidStack?, validate: Boolean = false) {
        if (stack == null) {
            if (this.getStack() == null) return
            this.stack = FluidStack.EMPTY
        } else if (!validate || isValid(stack)) {
            this.stack = stack.unwrap()
        } else {
            error("Invalid stack for tank: $stack ${stack.componentsPatch()}")
        }
        onContentsChanged()
    }

    override fun updateAmount(stack: ImmutableFluidStack, amount: Int) {
        this.stack.amount = amount
    }
}
