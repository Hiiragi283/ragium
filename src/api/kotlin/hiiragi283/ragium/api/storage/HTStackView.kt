package hiiragi283.ragium.api.storage

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.stack.ImmutableStack

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
}
