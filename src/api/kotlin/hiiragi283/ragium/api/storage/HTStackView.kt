package hiiragi283.ragium.api.storage

import hiiragi283.ragium.api.math.fixedFraction
import hiiragi283.ragium.api.stack.ImmutableStack
import org.apache.commons.lang3.math.Fraction
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
     * @return [Fraction]値での占有率
     */
    fun getStoredLevel(stack: STACK?): Fraction = fixedFraction(getAmount(), getCapacity(stack))

    override fun getAmount(): Int = getStack()?.amount() ?: 0

    override fun getCapacity(): Int = getCapacity(null)
}
