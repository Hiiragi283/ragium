package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.HTStorageStack
import hiiragi283.ragium.api.storage.fluid.HTFluidStorageStack
import java.util.function.BiPredicate
import java.util.function.LongSupplier
import java.util.function.Predicate

/**
 * @see [mekanism.common.capabilities.fluid.VariableCapacityFluidTank]
 */
class HTVariableFluidStackTank(
    private val capacitySupplier: LongSupplier,
    canExtract: BiPredicate<HTFluidStorageStack, HTStorageAccess>,
    canInsert: BiPredicate<HTFluidStorageStack, HTStorageAccess>,
    filter: Predicate<HTFluidStorageStack>,
    listener: HTContentListener?,
) : HTFluidStackTank(capacitySupplier.asLong, canExtract, canInsert, filter, listener) {
    companion object {
        @JvmStatic
        fun create(listener: HTContentListener?, capacity: LongSupplier): HTVariableFluidStackTank =
            HTVariableFluidStackTank(capacity, ALWAYS_TRUE, ALWAYS_TRUE, HTStorageStack.alwaysTrue(), listener)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            capacity: LongSupplier,
            canInsert: Predicate<HTFluidStorageStack> = HTStorageStack.alwaysTrue(),
            filter: Predicate<HTFluidStorageStack> = canInsert,
        ): HTVariableFluidStackTank = HTVariableFluidStackTank(
            capacity,
            { _, access: HTStorageAccess -> access != HTStorageAccess.EXTERNAL },
            { stack: HTFluidStorageStack, _ -> canInsert.test(stack) },
            filter,
            listener,
        )

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: LongSupplier): HTVariableFluidStackTank = HTVariableFluidStackTank(
            capacity,
            ALWAYS_TRUE,
            { _, access: HTStorageAccess -> access == HTStorageAccess.INTERNAl },
            HTStorageStack.alwaysTrue(),
            listener,
        )
    }

    override fun getCapacityAsLong(stack: HTFluidStorageStack): Long = capacitySupplier.asLong

    override fun setStackSize(amount: Int, action: HTStorageAction): Int {
        if (isEmpty()) {
            return 0
        } else if (amount <= 0) {
            if (action.execute) {
                setStack(HTFluidStorageStack.EMPTY)
            }
            return 0
        }
        val maxStackSize: Int = getCapacityAsInt(getStack())
        val fixedAmount: Int = if (maxStackSize in 1..<amount) {
            maxStackSize
        } else {
            amount
        }
        if (getAmountAsInt() == fixedAmount || !action.execute) {
            return fixedAmount
        }
        this.stack.amount = fixedAmount
        onContentsChanged()
        return fixedAmount
    }
}
