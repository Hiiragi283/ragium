package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.predicate.HTFluidPredicates
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.BiPredicate
import java.util.function.LongSupplier
import java.util.function.Predicate

/**
 * @see [mekanism.common.capabilities.fluid.VariableCapacityFluidTank]
 */
class HTVariableFluidStackTank(
    private val capacitySupplier: LongSupplier,
    canExtract: BiPredicate<FluidStack, HTStorageAccess>,
    canInsert: BiPredicate<FluidStack, HTStorageAccess>,
    filter: Predicate<FluidStack>,
    listener: HTContentListener?,
) : HTFluidStackTank(capacitySupplier.asLong, canExtract, canInsert, filter, listener) {
    companion object {
        @JvmStatic
        fun create(listener: HTContentListener?, capacity: LongSupplier): HTVariableFluidStackTank =
            HTVariableFluidStackTank(capacity, ALWAYS_TRUE, ALWAYS_TRUE, HTFluidPredicates.TRUE, listener)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            capacity: LongSupplier,
            canInsert: Predicate<FluidStack> = HTFluidPredicates.TRUE,
            filter: Predicate<FluidStack> = canInsert,
        ): HTVariableFluidStackTank = HTVariableFluidStackTank(
            capacity,
            { _: FluidStack, access: HTStorageAccess -> access != HTStorageAccess.EXTERNAL },
            { stack: FluidStack, _: HTStorageAccess -> canInsert.test(stack) },
            filter,
            listener,
        )

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: LongSupplier): HTVariableFluidStackTank = HTVariableFluidStackTank(
            capacity,
            ALWAYS_TRUE,
            { _: FluidStack, access: HTStorageAccess -> access == HTStorageAccess.INTERNAl },
            HTFluidPredicates.TRUE,
            listener,
        )
    }

    override fun getCapacityAsLong(stack: FluidStack): Long = capacitySupplier.asLong

    override fun setStackSize(amount: Int, simulate: Boolean): Int {
        if (isEmpty()) {
            return 0
        } else if (amount <= 0) {
            if (!simulate) {
                setEmpty()
            }
            return 0
        }
        val maxStackSize: Int = getCapacityAsInt(getStack())
        val fixedAmount: Int = if (maxStackSize in 1..<amount) {
            maxStackSize
        } else {
            amount
        }
        if (getAmountAsInt() == fixedAmount || simulate) {
            return fixedAmount
        }
        this.stack.amount = fixedAmount
        onContentsChanged()
        return fixedAmount
    }
}
