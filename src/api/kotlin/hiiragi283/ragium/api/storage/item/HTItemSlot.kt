package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.storage.HTStackSlot
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import kotlin.math.min

interface HTItemSlot : HTStackSlot<HTItemStorageStack> {
    companion object {
        const val ABSOLUTE_MAX_STACK_SIZE: Long = Item.ABSOLUTE_MAX_STACK_SIZE.toLong()
    }

    /**
     * GUIにおける[Slot]を返します。
     */
    fun createContainerSlot(): Slot?

    //    Mutable    //

    interface Mutable :
        HTItemSlot,
        HTStackSlot.Mutable<HTItemStorageStack> {
        override fun insert(stack: HTItemStorageStack, action: HTStorageAction, access: HTStorageAccess): HTItemStorageStack {
            if (stack.isEmpty()) return HTItemStorageStack.EMPTY

            val needed: Int = getNeededAsInt(stack)
            if (needed <= 0 || !isItemValidForInsert(stack, access)) return stack

            val sameType: Boolean = HTItemStorageStack.isSameItemSameComponents(getStack(), stack)
            if (isEmpty() || sameType) {
                val toAdd: Int = min(stack.amountAsInt(), needed)
                if (action.execute) {
                    if (sameType) {
                        growStack(toAdd, action)
                        onContentsChanged()
                    } else {
                        setStack(stack.copyWithAmount(toAdd))
                    }
                }
                return stack.copyWithAmount(stack.amountAsInt() - toAdd)
            }
            return stack
        }

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): HTItemStorageStack {
            val stack: HTItemStorageStack = getStack()
            if (isEmpty() || amount < 1 || !canItemExtract(getStack(), access)) {
                return HTItemStorageStack.EMPTY
            }
            val current: Int = min(stack.amountAsInt(), getCapacityAsInt(stack))
            val fixedAmount: Int = min(amount, current)
            val result: HTItemStorageStack = stack.copyWithAmount(fixedAmount)
            if (action.execute) {
                shrinkStack(fixedAmount, action)
                onContentsChanged()
            }
            return result
        }

        /**
         * 指定された[stack]をこのスロットに搬入できるか判定します。
         * @param stack 搬入される[HTItemStorageStack]
         * @param access このスロットへのアクセスの種類
         * @return 搬入できる場合は`true`
         */
        fun isItemValidForInsert(stack: HTItemStorageStack, access: HTStorageAccess): Boolean = isValid(stack)

        /**
         * 指定された[stack]をこのスロットに搬出できるか判定します。
         * @param stack 搬出される[HTItemStorageStack]
         * @param access このスロットへのアクセスの種類
         * @return 搬出できる場合は`true`
         */
        fun canItemExtract(stack: HTItemStorageStack, access: HTStorageAccess): Boolean = true

        override fun setStackSize(amount: Int, action: HTStorageAction): Int {
            if (isEmpty()) return 0
            if (amount <= 0) {
                if (action.execute) setStack(HTItemStorageStack.EMPTY)
                return 0
            }
            val stack: HTItemStorageStack = getStack()
            val maxStackSize: Int = getCapacityAsInt(stack)
            val fixedAmount: Int = min(amount, maxStackSize)
            if (stack.amountAsInt() == fixedAmount || !action.execute) {
                return fixedAmount
            }
            setStack(stack.copyWithAmount(fixedAmount))
            onContentsChanged()
            return fixedAmount
        }
    }
}
