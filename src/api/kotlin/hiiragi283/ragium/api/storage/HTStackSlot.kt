package hiiragi283.ragium.api.storage

import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableStack
import hiiragi283.ragium.api.util.HTContentListener
import kotlin.math.min

/**
 * 単一の[STACK]を保持し，出し入れが可能な[HTStackView]の拡張インターフェース
 * @param STACK 保持するスタックのクラス
 */
interface HTStackSlot<STACK : ImmutableStack<*, STACK>> :
    HTStackView<STACK>,
    HTValueSerializable,
    HTContentListener {
    /**
     * 指定した[stack]がスロットに有効かどうか判定します。
     * @return 有効な場合は`true`
     */
    fun isValid(stack: STACK): Boolean

    /**
     * 指定された引数から[STACK]を搬入します。
     * @param stack 搬入される[STACK]
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に搬入を行います。
     * @param access このスロットへのアクセスの種類
     * @return 搬入されなかった[STACK]
     */
    fun insert(stack: STACK, action: HTStorageAction, access: HTStorageAccess): STACK

    /**
     * 指定された引数から[STACK]を搬出します。
     * @param stack 搬出される[STACK]
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に搬出を行います。
     * @param access このスロットへのアクセスの種類
     * @return 搬出された[STACK]
     */
    fun extract(stack: STACK, action: HTStorageAction, access: HTStorageAccess): STACK

    /**
     * 指定された引数から[STACK]を搬出します。
     * @param amount 搬出する個数の最大値
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に搬出を行います。
     * @param access このスロットへのアクセスの種類
     * @return 搬出された[STACK]
     */
    fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): STACK

    /**
     * 指定された[other]と[getStack]が等価か判定します。
     */
    fun isSameStack(other: STACK): Boolean

    //    Mutable    //

    /**
     * 中身が可変な[HTStackSlot]の拡張クラス
     */
    abstract class Mutable<STACK : ImmutableStack<*, STACK>> :
        HTStackSlot<STACK>,
        HTStackView.Mutable<STACK> {
        /**
         * 空の[STACK]を返します。
         */
        protected abstract fun getEmptyStack(): STACK

        /**
         * 現在の[STACK]を空にします。
         */
        fun setEmpty() {
            setStack(getEmptyStack())
        }

        /**
         * 指定された[amount]から，現在の個数を置換します。
         * @param amount 置換する個数の最大値
         * @param action [HTStorageAction.EXECUTE]の場合のみ実際に置換を行います。
         * @return 実際に置換された個数
         */
        override fun setStackSize(amount: Int, action: HTStorageAction): Int {
            if (isEmpty()) return 0
            if (amount <= 0) {
                if (action.execute) setEmpty()
                return 0
            }
            val stack: STACK = getStack()
            val maxStackSize: Int = getCapacityAsInt(stack)
            val fixedAmount: Int = min(amount, maxStackSize)
            if (stack.amountAsInt() == fixedAmount || action.simulate) {
                return fixedAmount
            }
            updateCount(stack, fixedAmount)
            onContentsChanged()
            return fixedAmount
        }

        protected open fun updateCount(stack: STACK, amount: Int) {
            setStack(stack.copyWithAmount(amount))
        }

        override fun insert(stack: STACK, action: HTStorageAction, access: HTStorageAccess): STACK {
            if (stack.isEmpty()) return stack

            val needed: Int = getNeededAsInt(stack)
            if (needed <= 0 || !isStackValidForInsert(stack, access)) return stack

            val sameType: Boolean = isSameStack(stack)
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

        final override fun extract(stack: STACK, action: HTStorageAction, access: HTStorageAccess): STACK = when {
            this.isNotEmpty() && isSameStack(stack) -> extract(stack.amountAsInt(), action, access)
            else -> getEmptyStack()
        }

        override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): STACK {
            val stack: STACK = this.getStack()
            if (isEmpty() || amount < 1 || !canStackExtract(stack, access)) return getEmptyStack()
            val fixedAmount: Int = min(amount, getAmountAsInt())
            val result: STACK = stack.copyWithAmount(fixedAmount)
            if (result.isNotEmpty() && action.execute) {
                shrinkStack(fixedAmount, action)
                onContentsChanged()
            }
            return result
        }

        /**
         * 指定された[stack]をこのスロットに搬入できるか判定します。
         * @param stack 搬入される[STACK]
         * @param access このスロットへのアクセスの種類
         * @return 搬入できる場合は`true`
         */
        protected open fun isStackValidForInsert(stack: STACK, access: HTStorageAccess): Boolean = isValid(stack)

        /**
         * 指定された[stack]をこのスロットに搬出できるか判定します。
         * @param stack 搬出される[STACK]
         * @param access このスロットへのアクセスの種類
         * @return 搬出できる場合は`true`
         */
        protected open fun canStackExtract(stack: STACK, access: HTStorageAccess): Boolean = true
    }
}
