package hiiragi283.ragium.api.storage

import hiiragi283.ragium.api.stack.ImmutableStack
import kotlin.math.max

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
    fun getNeeded(stack: STACK?): Int = max(0, getCapacity(stack) - getAmount())

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

    override fun getCapacity(): Int = getCapacity(null)
}
