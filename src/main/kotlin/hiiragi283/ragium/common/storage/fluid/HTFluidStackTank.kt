package hiiragi283.ragium.common.storage.fluid

import com.google.common.base.Predicates
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.BiCodecs
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.value.HTValueInput
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @see [mekanism.common.capabilities.fluid.BasicFluidTank]
 */
open class HTFluidStackTank protected constructor(
    private val capacity: Int,
    private val canExtract: BiPredicate<FluidStack, HTStorageAccess>,
    private val canInsert: BiPredicate<FluidStack, HTStorageAccess>,
    private val filter: Predicate<FluidStack>,
    private val listener: HTContentListener?,
) : HTFluidTank {
    companion object {
        @JvmField
        val ALWAYS_TRUE: BiPredicate<FluidStack, HTStorageAccess> =
            BiPredicate { _: FluidStack, _: HTStorageAccess -> true }

        @JvmStatic
        private fun validateCapacity(capacity: Int): Int {
            check(capacity >= 0) { "Capacity must be non negative" }
            return capacity
        }

        @JvmStatic
        fun create(listener: HTContentListener?, capacity: Int): HTFluidStackTank =
            HTFluidStackTank(validateCapacity(capacity), ALWAYS_TRUE, ALWAYS_TRUE, Predicates.alwaysTrue(), listener)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            capacity: Int,
            canInsert: Predicate<FluidStack> = Predicates.alwaysTrue(),
            filter: Predicate<FluidStack> = canInsert,
        ): HTFluidStackTank = HTFluidStackTank(
            validateCapacity(capacity),
            { _: FluidStack, access: HTStorageAccess -> access != HTStorageAccess.EXTERNAL },
            { stack: FluidStack, _: HTStorageAccess -> canInsert.test(stack) },
            filter,
            listener,
        )

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: Int): HTFluidStackTank = HTFluidStackTank(
            validateCapacity(capacity),
            ALWAYS_TRUE,
            { _: FluidStack, access: HTStorageAccess -> access == HTStorageAccess.INTERNAl },
            Predicates.alwaysTrue(),
            listener,
        )
    }

    @JvmField
    protected var stack: FluidStack = FluidStack.EMPTY

    override fun getStack(): FluidStack = stack

    override fun setStack(stack: FluidStack) {
        setStackUnchecked(stack, true)
    }

    private fun setStackUnchecked(stack: FluidStack, validate: Boolean = false) {
        if (stack.isEmpty) {
            if (getStack().isEmpty) return
            this.stack = FluidStack.EMPTY
        } else if (!validate || isFluidValid(stack)) {
            this.stack = stack.copy()
        } else {
            error("Invalid stack for tank: $stack ${stack.componentsPatch}")
        }
        onContentsChanged()
    }

    override fun isFluidValidForInsert(stack: FluidStack, access: HTStorageAccess): Boolean =
        super.isFluidValidForInsert(stack, access) && canInsert.test(stack, access)

    override fun canFluidExtract(stack: FluidStack, access: HTStorageAccess): Boolean = canExtract.test(stack, access)

    override fun getCapacity(): Int = capacity

    override fun isFluidValid(stack: FluidStack): Boolean = filter.test(stack)

    override fun deserialize(input: HTValueInput) {
        input.read(RagiumConst.FLUID, BiCodecs.fluidStack(true))?.let(::setStackUnchecked)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }
}
