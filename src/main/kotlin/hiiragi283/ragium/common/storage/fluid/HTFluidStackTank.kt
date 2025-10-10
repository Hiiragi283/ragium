package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageStack
import hiiragi283.ragium.api.storage.fluid.HTFluidStorageStack
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.value.HTValueInput
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @see [mekanism.common.capabilities.fluid.BasicFluidTank]
 */
open class HTFluidStackTank protected constructor(
    private val capacity: Long,
    private val canExtract: BiPredicate<HTFluidStorageStack, HTStorageAccess>,
    private val canInsert: BiPredicate<HTFluidStorageStack, HTStorageAccess>,
    private val filter: Predicate<HTFluidStorageStack>,
    private val listener: HTContentListener?,
) : HTFluidTank.Mutable {
    companion object {
        @JvmField
        val ALWAYS_TRUE: BiPredicate<HTFluidStorageStack, HTStorageAccess> =
            BiPredicate { _, _ -> true }

        @JvmStatic
        private fun validateCapacity(capacity: Long): Long {
            check(capacity >= 0) { "Capacity must be non negative" }
            return capacity
        }

        @JvmStatic
        fun create(listener: HTContentListener?, capacity: Long): HTFluidStackTank =
            HTFluidStackTank(validateCapacity(capacity), ALWAYS_TRUE, ALWAYS_TRUE, HTStorageStack.alwaysTrue(), listener)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            capacity: Long,
            canInsert: Predicate<HTFluidStorageStack> = HTStorageStack.alwaysTrue(),
            filter: Predicate<HTFluidStorageStack> = canInsert,
        ): HTFluidStackTank = HTFluidStackTank(
            validateCapacity(capacity),
            { _, access: HTStorageAccess -> access != HTStorageAccess.EXTERNAL },
            { stack: HTFluidStorageStack, _ -> canInsert.test(stack) },
            filter,
            listener,
        )

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: Long): HTFluidStackTank = HTFluidStackTank(
            validateCapacity(capacity),
            ALWAYS_TRUE,
            { _, access: HTStorageAccess -> access == HTStorageAccess.INTERNAl },
            HTStorageStack.alwaysTrue(),
            listener,
        )
    }

    @JvmField
    protected var stack: FluidStack = FluidStack.EMPTY

    override fun getStack(): HTFluidStorageStack = HTFluidStorageStack.of(stack)

    override fun getCapacityAsLong(stack: HTFluidStorageStack): Long = capacity

    override fun isValid(stack: HTFluidStorageStack): Boolean = filter.test(stack)

    override fun isFluidValidForInsert(stack: HTFluidStorageStack, access: HTStorageAccess): Boolean =
        super.isFluidValidForInsert(stack, access) && canInsert.test(stack, access)

    override fun canFluidExtract(stack: HTFluidStorageStack, access: HTStorageAccess): Boolean =
        super.canFluidExtract(stack, access) && canExtract.test(stack, access)

    override fun deserialize(input: HTValueInput) {
        input.read(RagiumConst.FLUID, HTFluidStorageStack.CODEC)?.let(::setStackUnchecked)
    }

    final override fun onContentsChanged() {
        listener?.onContentsChanged()
    }

    override fun setStack(stack: HTFluidStorageStack) {
        setStackUnchecked(stack, true)
    }

    fun setStackUnchecked(stack: HTFluidStorageStack, validate: Boolean = false) {
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
