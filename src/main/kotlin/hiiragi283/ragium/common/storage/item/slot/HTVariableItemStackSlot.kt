package hiiragi283.ragium.common.storage.item.slot

import hiiragi283.ragium.api.inventory.HTContainerItemSlot
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.util.HTContentListener
import java.util.function.BiPredicate
import java.util.function.Predicate
import java.util.function.ToIntFunction

class HTVariableItemStackSlot(
    private val capacityFunction: ToIntFunction<ImmutableItemStack>,
    canExtract: BiPredicate<ImmutableItemStack, HTStorageAccess>,
    canInsert: BiPredicate<ImmutableItemStack, HTStorageAccess>,
    filter: Predicate<ImmutableItemStack>,
    listener: HTContentListener?,
    x: Int,
    y: Int,
    slotType: HTContainerItemSlot.Type,
) : HTItemStackSlot(
        capacityFunction.applyAsInt(null),
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
            capacity: ToIntFunction<ImmutableItemStack>,
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
            capacity: ToIntFunction<ImmutableItemStack>,
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
            capacity: ToIntFunction<ImmutableItemStack>,
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

    override fun getCapacity(stack: ImmutableItemStack?): Int = stack?.let(capacityFunction::applyAsInt) ?: 0

    override fun setStackSize(amount: Int, action: HTStorageAction): Int {
        if (this.getStack() == null) {
            return 0
        } else if (amount <= 0) {
            if (action.execute) {
                setEmpty()
            }
            return 0
        }
        val maxStackSize: Int = getCapacity()
        val fixedAmount: Int = if (maxStackSize in 1..<amount) {
            maxStackSize
        } else {
            amount
        }
        if (getAmount() == fixedAmount || action.simulate) {
            return fixedAmount
        }
        this.stack.count = fixedAmount
        onContentsChanged()
        return fixedAmount
    }
}
