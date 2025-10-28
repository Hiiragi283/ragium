package hiiragi283.ragium.api.storage

import hiiragi283.ragium.api.stack.ImmutableStack
import kotlin.math.min

/**
 * 単一の[STACK]を保持するインターフェース
 * @param STACK 保持するスタックのクラス
 */
interface HTStackView<STACK : ImmutableStack<*, STACK>> : HTAmountView.IntSized {
    /**
     * 保持している[STACK]を返します。
     */
    fun getStack(): STACK?

    /**
     * このスロットの容量を返します。
     * @return [Int]値での容量
     */
    fun getCapacity(stack: STACK?): Int

    /**
     * このスロットの空き容量を返します。
     * @return [Int]値での空き容量
     */
    fun getNeededAsInt(stack: STACK?): Int = getCapacity(stack) - getAmount()

    /**
     * このスロットの占有率を返します。
     * @return [Double]値での占有率
     */
    fun getStoredLevelAsDouble(stack: STACK?): Double {
        val capacity: Int = getCapacity(stack)
        if (capacity <= 0) return 0.0
        return getAmount() / capacity.toDouble()
    }

    /**
     * このスロットの占有率を返します。
     * @return [Float]値での占有率
     */
    fun getStoredLevelAsFloat(stack: STACK?): Float {
        val capacity: Int = getCapacity(stack)
        if (capacity <= 0) return 0f
        return getAmount() / capacity.toFloat()
    }

    override fun getAmount(): Int = getStack()?.amount() ?: 0

    override fun getCapacity(): Int = getCapacity(getStack())

    //    Mutable    //

    interface Mutable<STACK : ImmutableStack<*, STACK>> : HTStackView<STACK> {
        /**
         * 指定された[stack]を保持します。
         */
        fun setStack(stack: STACK?)

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
            val current: Int = getAmount()
            if (current == 0) return 0
            val fixedAmount: Int = if (amount > 0) {
                min(amount, getCapacity())
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
