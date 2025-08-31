package hiiragi283.ragium.common.storage.fluid

import com.google.common.base.Predicates
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
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
        fun atManualOut(
            listener: HTContentListener?,
            capacity: Int,
            canExtract: Predicate<FluidStack> = Predicates.alwaysTrue(),
            canInsert: Predicate<FluidStack> = Predicates.alwaysTrue(),
            filter: Predicate<FluidStack> = Predicates.alwaysTrue(),
        ): HTFluidStackTank = HTFluidStackTank(capacity, canExtract, canInsert, filter, listener)

        @JvmStatic
        fun of(
            listener: HTContentListener?,
            capacity: Int,
            canExtract: BiPredicate<FluidStack, HTStorageAccess> = ALWAYS_TRUE,
            canInsert: BiPredicate<FluidStack, HTStorageAccess> = ALWAYS_TRUE,
            filter: Predicate<FluidStack> = Predicates.alwaysTrue(),
        ): HTFluidStackTank = HTFluidStackTank(capacity, canExtract, canInsert, filter, listener)
    }

    protected constructor(
        capacity: Int,
        canExtract: Predicate<FluidStack>,
        canInsert: Predicate<FluidStack>,
        filter: Predicate<FluidStack>,
        listener: HTContentListener?,
    ) : this(
        capacity,
        { stack: FluidStack, access: HTStorageAccess -> access == HTStorageAccess.MANUAL && canExtract.test(stack) },
        { stack: FluidStack, _: HTStorageAccess -> canInsert.test(stack) },
        filter,
        listener,
    )

    private var stack: FluidStack = FluidStack.EMPTY

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

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        setStackUnchecked(FluidStack.parseOptional(provider, nbt.getCompound(RagiumConst.FLUID)), false)
    }

    override fun onContentsChanged() {
        listener?.onContentsChanged()
    }
}
