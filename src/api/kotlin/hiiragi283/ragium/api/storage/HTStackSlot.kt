package hiiragi283.ragium.api.storage

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import kotlin.math.min

/**
 * 単一の[STACK]を保持するスロットのインターフェース
 * @param STACK 保持するスタックのクラス
 */
interface HTStackSlot<STACK : HTStorageStack<*, STACK>> :
    HTValueSerializable,
    HTContentListener {
    /**
     * 保持している[STACK]を返します。
     */
    fun getStack(): STACK

    /**
     * このスロットが空かどうか判定します。
     * @return 空の場合は`true`
     */
    fun isEmpty(): Boolean = getStack().isEmpty()

    /**
     * このスロットが保持している個数を返します。
     * @return [Long]値での個数
     */
    fun getAmountAsLong(): Long = getStack().amountAsLong()

    /**
     * このスロットが保持している個数を返します。
     * @return [Int]値での個数
     */
    fun getAmountAsInt(): Int = getStack().amountAsInt()

    /**
     * このスロットの容量を返します。
     * @return [Long]値での容量
     */
    fun getCapacityAsLong(stack: STACK): Long

    /**
     * このスロットの容量を返します。
     * @return [Int]値での容量
     */
    fun getCapacityAsInt(stack: STACK): Int = Ints.saturatedCast(getCapacityAsLong(stack))

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
     * @param amount 搬出する個数の最大値
     * @param action [HTStorageAction.EXECUTE]の場合のみ実際に搬出を行います。
     * @param access このスロットへのアクセスの種類
     * @return 搬出された[STACK]
     */
    fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): STACK

    /**
     * このスロットの空き容量を返します。
     * @return [Long]値での空き容量
     */
    fun getNeededAsLong(stack: STACK): Long = getCapacityAsLong(stack) - getAmountAsLong()

    /**
     * このスロットの空き容量を返します。
     * @return [Int]値での空き容量
     */
    fun getNeededAsInt(stack: STACK): Int = getCapacityAsInt(stack) - getAmountAsInt()

    /**
     * このスロットの占有率を返します。
     * @return [Double]値での占有率
     */
    fun getStoredLevelAsDouble(stack: STACK): Double = getAmountAsLong() / getCapacityAsLong(stack).toDouble()

    /**
     * このスロットの占有率を返します。
     * @return [Float]値での占有率
     */
    fun getStoredLevelAsFloat(stack: STACK): Float = getAmountAsLong() / getCapacityAsLong(stack).toFloat()

    //    Mutable    //

    /**
     * 中身が可変な[HTStackSlot]の拡張クラス
     */
    abstract class Mutable<STACK : HTStorageStack<*, STACK>> : HTStackSlot<STACK> {
        /**
         * 指定された[stack]を保持します。
         */
        abstract fun setStack(stack: STACK)

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
        open fun setStackSize(amount: Int, action: HTStorageAction): Int {
            if (isEmpty()) return 0
            if (amount <= 0) {
                if (action.execute) setEmpty()
                return 0
            }
            val stack: STACK = getStack()
            val maxStackSize: Int = getCapacityAsInt(stack)
            val fixedAmount: Int = min(amount, maxStackSize)
            if (stack.amountAsInt() == fixedAmount || !action.execute) {
                return fixedAmount
            }
            setStack(stack.copyWithAmount(fixedAmount))
            onContentsChanged()
            return fixedAmount
        }

        /**
         * 指定された[amount]から，現在の個数に追加します。
         * @param amount 追加する個数の最大値
         * @param action [HTStorageAction.EXECUTE]の場合のみ実際に追加を行います。
         * @return 実際に追加された個数
         */
        fun growStack(amount: Int, action: HTStorageAction): Int {
            val current: Int = getAmountAsInt()
            if (current == 0) return 0
            val fixedAmount: Int = if (amount > 0) {
                min(amount, getCapacityAsInt(getStack()))
            } else {
                amount
            }
            val newSize: Int = setStackSize(current + fixedAmount, action)
            return newSize - current
        }

        /**
         * 指定された[amount]から，現在の個数を削除します。
         * @param amount 削除する個数の最大値
         * @param action [HTStorageAction.EXECUTE]の場合のみ実際に削除を行います。
         * @return 実際に削除された個数
         */
        fun shrinkStack(amount: Int, action: HTStorageAction): Int = -growStack(-amount, action)

        final override fun insert(stack: STACK, action: HTStorageAction, access: HTStorageAccess): STACK {
            if (stack.isEmpty()) return getEmptyStack()

            val needed: Int = getNeededAsInt(stack)
            if (needed <= 0 || !isStackValidForInsert(stack, access)) return stack

            val sameType: Boolean = isSameStack(getStack(), stack)
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

        final override fun extract(amount: Int, action: HTStorageAction, access: HTStorageAccess): STACK {
            val stack: STACK = getStack()
            if (isEmpty() || amount < 1 || !canStackExtract(getStack(), access)) {
                return getEmptyStack()
            }
            val current: Int = min(stack.amountAsInt(), getCapacityAsInt(getStack()))
            val fixedAmount: Int = min(amount, current)
            val result: STACK = stack.copyWithAmount(fixedAmount)
            if (action.execute) {
                shrinkStack(fixedAmount, action)
                onContentsChanged()
            }
            return result
        }

        /**
         * 指定された[first]と[second]が等価か判定します。
         */
        protected abstract fun isSameStack(first: STACK, second: STACK): Boolean

        /**
         * 指定された[stack]をこのスロットに搬入できるか判定します。
         * @param stack 搬入される[STACK]
         * @param access このスロットへのアクセスの種類
         * @return 搬入できる場合は`true`
         */
        open fun isStackValidForInsert(stack: STACK, access: HTStorageAccess): Boolean = isValid(stack)

        /**
         * 指定された[stack]をこのスロットに搬出できるか判定します。
         * @param stack 搬出される[STACK]
         * @param access このスロットへのアクセスの種類
         * @return 搬出できる場合は`true`
         */
        open fun canStackExtract(stack: STACK, access: HTStorageAccess): Boolean = true
    }
}
