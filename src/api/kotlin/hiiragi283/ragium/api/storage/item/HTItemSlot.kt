package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.storage.HTStackSlot
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.value.HTValueOutput
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import kotlin.math.min

interface HTItemSlot : HTStackSlot<ItemStack> {
    companion object {
        const val ABSOLUTE_MAX_STACK_SIZE: Long = Item.ABSOLUTE_MAX_STACK_SIZE.toLong()
    }

    override fun getAmountAsLong(): Long = getStack().count.toLong()

    override fun isEmpty(): Boolean = getStack().isEmpty

    override fun serialize(output: HTValueOutput) {
        output.store(RagiumConst.ITEM, BiCodecs.itemStack(true), getStack())
    }

    /**
     * GUIにおける[Slot]を返します。
     */
    fun createContainerSlot(): Slot?

    //    Mutable    //

    interface Mutable :
        HTItemSlot,
        HTStackSlot.Mutable<ItemStack> {
        override fun insert(stack: ItemStack, action: HTStorageAction, access: HTStorageAccess): ItemStack {
            if (stack.isEmpty) return ItemStack.EMPTY

            val needed: Int = getNeededAsInt(stack)
            if (needed <= 0 || !isItemValidForInsert(stack, access)) return stack

            val sameType: Boolean = ItemStack.isSameItemSameComponents(getStack(), stack)
            if (isEmpty() || sameType) {
                val toAdd: Int = min(stack.count, needed)
                if (action.execute) {
                    if (sameType) {
                        growStack(toAdd, action)
                        onContentsChanged()
                    } else {
                        setStack(stack.copyWithCount(toAdd))
                    }
                }
                return stack.copyWithCount(stack.count - toAdd)
            }
            return stack
        }

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): ItemStack {
            val stack: ItemStack = getStack()
            if (isEmpty() || amount < 1 || !canItemExtract(getStack(), access)) {
                return ItemStack.EMPTY
            }
            val current: Int = min(stack.count, stack.maxStackSize)
            val fixedAmount: Int = min(amount, current)
            val result: ItemStack = stack.copyWithCount(fixedAmount)
            if (action.execute) {
                shrinkStack(fixedAmount, action)
                onContentsChanged()
            }
            return result
        }

        /**
         * 指定された[stack]をこのスロットに搬入できるか判定します。
         * @param stack 搬入される[ItemStack]
         * @param access このスロットへのアクセスの種類
         * @return 搬入できる場合は`true`
         */
        fun isItemValidForInsert(stack: ItemStack, access: HTStorageAccess): Boolean = isValid(stack)

        /**
         * 指定された[stack]をこのスロットに搬出できるか判定します。
         * @param stack 搬出される[ItemStack]
         * @param access このスロットへのアクセスの種類
         * @return 搬出できる場合は`true`
         */
        fun canItemExtract(stack: ItemStack, access: HTStorageAccess): Boolean = true

        /**
         * 指定された[amount]から，現在の個数を置換します。
         * @param amount 置換する個数の最大値
         * @param action [HTStorageAction.EXECUTE]の場合のみ実際に置換を行います。
         * @return 実際に置換された個数
         */
        override fun setStackSize(amount: Int, action: HTStorageAction): Int {
            if (isEmpty()) return 0
            if (amount <= 0) {
                if (action.execute) setStack(ItemStack.EMPTY)
                return 0
            }
            val stack: ItemStack = getStack()
            val maxStackSize: Int = getCapacityAsInt(stack)
            val fixedAmount: Int = min(amount, maxStackSize)
            if (stack.count == fixedAmount || !action.execute) {
                return fixedAmount
            }
            setStack(stack.copyWithCount(fixedAmount))
            onContentsChanged()
            return fixedAmount
        }
    }
}
