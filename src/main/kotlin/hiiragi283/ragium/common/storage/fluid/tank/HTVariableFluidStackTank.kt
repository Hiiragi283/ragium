package hiiragi283.ragium.common.storage.fluid.tank

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import java.util.function.BiPredicate
import java.util.function.LongSupplier
import java.util.function.Predicate

/**
 * @see [mekanism.common.capabilities.fluid.VariableCapacityFluidTank]
 */
class HTVariableFluidStackTank(
    private val capacitySupplier: LongSupplier,
    canExtract: BiPredicate<ImmutableFluidStack, HTStorageAccess>,
    canInsert: BiPredicate<ImmutableFluidStack, HTStorageAccess>,
    filter: Predicate<ImmutableFluidStack>,
    listener: HTContentListener?,
) : HTFluidStackTank(capacitySupplier.asLong, canExtract, canInsert, filter, listener) {
    companion object {
        @JvmStatic
        fun create(listener: HTContentListener?, capacity: LongSupplier): HTVariableFluidStackTank =
            HTVariableFluidStackTank(capacity, ALWAYS_TRUE, ALWAYS_TRUE, ImmutableStack.alwaysTrue(), listener)

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            capacity: LongSupplier,
            canInsert: Predicate<ImmutableFluidStack> = ImmutableStack.alwaysTrue(),
            filter: Predicate<ImmutableFluidStack> = canInsert,
        ): HTVariableFluidStackTank = HTVariableFluidStackTank(
            capacity,
            { _, access: HTStorageAccess -> access != HTStorageAccess.EXTERNAL },
            { stack: ImmutableFluidStack, _ -> canInsert.test(stack) },
            filter,
            listener,
        )

        @JvmStatic
        fun output(listener: HTContentListener?, capacity: LongSupplier): HTVariableFluidStackTank = HTVariableFluidStackTank(
            capacity,
            ALWAYS_TRUE,
            { _, access: HTStorageAccess -> access == HTStorageAccess.INTERNAL },
            ImmutableStack.alwaysTrue(),
            listener,
        )
    }

    override fun getCapacityAsLong(stack: ImmutableFluidStack): Long = capacitySupplier.asLong

    override fun setStackSize(amount: Int, action: HTStorageAction): Int {
        if (isEmpty()) {
            return 0
        } else if (amount <= 0) {
            if (action.execute) {
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
        if (getAmountAsInt() == fixedAmount || action.simulate) {
            return fixedAmount
        }
        this.stack.amount = fixedAmount
        onContentsChanged()
        return fixedAmount
    }
}
