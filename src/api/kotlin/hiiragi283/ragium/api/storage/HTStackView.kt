package hiiragi283.ragium.api.storage

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.stack.ImmutableStack
import kotlin.math.min

/**
 * 単一の[STACK]を保持するインターフェース
 * @param STACK 保持するスタックのクラス
 */
interface HTStackView<STACK : ImmutableStack<*, STACK>> {
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
    fun getStoredLevelAsDouble(stack: STACK): Double {
        val capacity: Long = getCapacityAsLong(stack)
        if (capacity <= 0L) return 0.0
        return getAmountAsLong() / capacity.toDouble()
    }

    /**
     * このスロットの占有率を返します。
     * @return [Float]値での占有率
     */
    fun getStoredLevelAsFloat(stack: STACK): Float {
        val capacity: Long = getCapacityAsLong(stack)
        if (capacity <= 0L) return 0f
        return getAmountAsLong() / capacity.toFloat()
    }

    //    Mutable    //

    interface Mutable<STACK : ImmutableStack<*, STACK>> : HTStackView<STACK> {
        /**
         * 指定された[stack]を保持します。
         */
        fun setStack(stack: STACK)

        /**
         * 指定された[amount]から，現在の個数を置換します。
         * @param amount 置換する個数の最大値
         * @param action [HTStorageAction.EXECUTE]の場合のみ実際に置換を行います。
         * @return 実際に置換された個数
         */
        fun setStackSize(amount: Int, action: HTStorageAction): Int

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
    }
}
