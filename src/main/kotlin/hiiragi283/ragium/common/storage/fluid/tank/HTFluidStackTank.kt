package hiiragi283.ragium.common.storage.fluid.tank

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.util.HTContentListener
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @see [mekanism.common.capabilities.fluid.BasicFluidTank]
 */
open class HTFluidStackTank protected constructor(
    private val capacity: Long,
    private val canExtract: BiPredicate<ImmutableFluidStack, HTStorageAccess>,
    private val canInsert: BiPredicate<ImmutableFluidStack, HTStorageAccess>,
    private val filter: Predicate<ImmutableFluidStack>,
    private val listener: HTContentListener?,
) : HTFluidTank.Mutable() {
    companion object {
        @JvmField
        val ALWAYS_TRUE: BiPredicate<ImmutableFluidStack, HTStorageAccess> =
            BiPredicate { _, _ -> true }

        @JvmStatic
        private fun validateCapacity(capacity: Long): Long {
            check(capacity >= 0) { "Capacity must be non negative" }
            return capacity
        }

        @JvmStatic
        fun create(listener: HTContentListener?, capacity: Long): HTFluidStackTank =
            HTFluidStackTank(validateCapacity(capacity), ALWAYS_TRUE, ALWAYS_TRUE, ImmutableStack.alwaysTrue(), listener)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            capacity: Long,
            canInsert: Predicate<ImmutableFluidStack> = ImmutableStack.alwaysTrue(),
            filter: Predicate<ImmutableFluidStack> = canInsert,
        ): HTFluidStackTank = HTFluidStackTank(
            validateCapacity(capacity),
            { _, access: HTStorageAccess -> access != HTStorageAccess.EXTERNAL },
            { stack: ImmutableFluidStack, _ -> canInsert.test(stack) },
            filter,
            listener,
        )

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: Long): HTFluidStackTank = HTFluidStackTank(
            validateCapacity(capacity),
            ALWAYS_TRUE,
            { _, access: HTStorageAccess -> access == HTStorageAccess.INTERNAL },
            ImmutableStack.alwaysTrue(),
            listener,
        )
    }

    @JvmField
    protected var stack: FluidStack = FluidStack.EMPTY

    override fun getStack(): ImmutableFluidStack = stack.toImmutable()

    override fun getCapacityAsLong(stack: ImmutableFluidStack): Long = capacity

    override fun isValid(stack: ImmutableFluidStack): Boolean = filter.test(stack)

    override fun isStackValidForInsert(stack: ImmutableFluidStack, access: HTStorageAccess): Boolean =
        super.isStackValidForInsert(stack, access) && canInsert.test(stack, access)

    override fun canStackExtract(stack: ImmutableFluidStack, access: HTStorageAccess): Boolean =
        super.canStackExtract(stack, access) && canExtract.test(stack, access)

    override fun serialize(output: HTValueOutput) {
        output.store(RagiumConst.FLUID, ImmutableFluidStack.CODEC, getStack())
    }

    override fun deserialize(input: HTValueInput) {
        input.read(RagiumConst.FLUID, ImmutableFluidStack.CODEC)?.let(::setStackUnchecked)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }

    override fun setStack(stack: ImmutableFluidStack) {
        setStackUnchecked(stack, true)
    }

    fun setStackUnchecked(stack: ImmutableFluidStack, validate: Boolean = false) {
        if (stack.isEmpty()) {
            if (this.isEmpty()) return
            this.stack = FluidStack.EMPTY
        } else if (!validate || isValid(stack)) {
            this.stack = stack.copy().stack
        } else {
            error("Invalid stack for tank: $stack ${stack.componentsPatch()}")
        }
        onContentsChanged()
    }
}
