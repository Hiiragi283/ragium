package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.predicate.HTFluidPredicates
import hiiragi283.ragium.api.storage.value.HTValueInput
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @see [mekanism.common.capabilities.fluid.BasicFluidTank]
 */
open class HTFluidStackTank protected constructor(
    private val capacity: Long,
    private val canExtract: BiPredicate<FluidStack, HTStorageAccess>,
    private val canInsert: BiPredicate<FluidStack, HTStorageAccess>,
    private val filter: Predicate<FluidStack>,
    private val listener: HTContentListener?,
) : HTFluidTank.Mutable {
    companion object {
        @JvmField
        val ALWAYS_TRUE: BiPredicate<FluidStack, HTStorageAccess> =
            BiPredicate { _: FluidStack, _: HTStorageAccess -> true }

        @JvmStatic
        private fun validateCapacity(capacity: Long): Long {
            check(capacity >= 0) { "Capacity must be non negative" }
            return capacity
        }

        @JvmStatic
        fun create(listener: HTContentListener?, capacity: Long): HTFluidStackTank =
            HTFluidStackTank(validateCapacity(capacity), ALWAYS_TRUE, ALWAYS_TRUE, HTFluidPredicates.TRUE, listener)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            capacity: Long,
            canInsert: Predicate<FluidStack> = HTFluidPredicates.TRUE,
            filter: Predicate<FluidStack> = canInsert,
        ): HTFluidStackTank = HTFluidStackTank(
            validateCapacity(capacity),
            { _: FluidStack, access: HTStorageAccess -> access != HTStorageAccess.EXTERNAL },
            { stack: FluidStack, _: HTStorageAccess -> canInsert.test(stack) },
            filter,
            listener,
        )

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: Long): HTFluidStackTank = HTFluidStackTank(
            validateCapacity(capacity),
            ALWAYS_TRUE,
            { _: FluidStack, access: HTStorageAccess -> access == HTStorageAccess.INTERNAl },
            HTFluidPredicates.TRUE,
            listener,
        )
    }

    @JvmField
    protected var stack: FluidStack = FluidStack.EMPTY

    override fun getStack(): FluidStack = stack

    override fun getCapacityAsLong(stack: FluidStack): Long = capacity

    override fun isValid(stack: FluidStack): Boolean = filter.test(stack)

    override fun deserialize(input: HTValueInput) {
        input.read(RagiumConst.FLUID, BiCodecs.fluidStack(true))?.let(::setStackUnchecked)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }

    override fun setStack(stack: FluidStack) {
        setStackUnchecked(stack, true)
    }

    fun setStackUnchecked(stack: FluidStack, validate: Boolean = false) {
        if (stack.isEmpty) {
            if (getStack().isEmpty) return
            this.stack = FluidStack.EMPTY
        } else if (!validate || isValid(stack)) {
            this.stack = stack.copy()
        } else {
            error("Invalid stack for tank: $stack ${stack.componentsPatch}")
        }
        onContentsChanged()
    }
}
