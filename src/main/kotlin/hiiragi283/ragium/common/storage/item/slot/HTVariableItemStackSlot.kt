package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import java.util.function.BiPredicate
import java.util.function.Predicate
import java.util.function.ToLongFunction

class HTVariableItemStackSlot(
    private val capacityFunction: ToLongFunction<ImmutableItemStack>,
    canExtract: BiPredicate<ImmutableItemStack, HTStorageAccess>,
    canInsert: BiPredicate<ImmutableItemStack, HTStorageAccess>,
    filter: Predicate<ImmutableItemStack>,
    listener: HTContentListener?,
    x: Int,
    y: Int,
    slotType: HTContainerItemSlot.Type,
) : HTItemStackSlot(
        capacityFunction.applyAsLong(ImmutableItemStack.EMPTY),
        canExtract,
        canInsert,
        filter,
        listener,
        x,
        y,
        slotType,
    ) {
    companion object {
        @JvmStatic
        fun create(
            listener: HTContentListener?,
            capacity: ToLongFunction<ImmutableItemStack>,
            x: Int,
            y: Int,
        ): HTVariableItemStackSlot = HTVariableItemStackSlot(
            capacity,
            ALWAYS_TRUE,
            ALWAYS_TRUE,
            ImmutableStack.alwaysTrue(),
            listener,
            x,
            y,
            HTContainerItemSlot.Type.BOTH,
        )

        @JvmStatic
        fun input(
            listener: HTContentListener?,
            capacity: ToLongFunction<ImmutableItemStack>,
            canInsert: Predicate<ImmutableItemStack> = ImmutableStack.alwaysTrue(),
            filter: Predicate<ImmutableItemStack> = canInsert,
            x: Int,
            y: Int,
        ): HTVariableItemStackSlot = HTVariableItemStackSlot(
            capacity,
            { _, access: HTStorageAccess -> access != HTStorageAccess.EXTERNAL },
            { stack: ImmutableItemStack, _ -> canInsert.test(stack) },
            filter,
            listener,
            x,
            y,
            HTContainerItemSlot.Type.INPUT,
        )

        @JvmStatic
        fun output(
            listener: HTContentListener?,
            capacity: ToLongFunction<ImmutableItemStack>,
            x: Int,
            y: Int,
        ): HTVariableItemStackSlot = HTVariableItemStackSlot(
            capacity,
            ALWAYS_TRUE,
            { _, access: HTStorageAccess -> access == HTStorageAccess.INTERNAL },
            ImmutableStack.alwaysTrue(),
            listener,
            x,
            y,
            HTContainerItemSlot.Type.OUTPUT,
        )
    }

    override fun getCapacityAsLong(stack: ImmutableItemStack): Long = capacityFunction.applyAsLong(stack)

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
        this.stack.count = fixedAmount
        onContentsChanged()
        return fixedAmount
    }
}
