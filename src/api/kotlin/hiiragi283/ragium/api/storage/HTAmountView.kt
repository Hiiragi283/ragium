package hiiragi283.ragium.api.storage

import com.google.common.primitives.Ints
import hiiragi283.ragium.api.math.fixedFraction
import org.apache.commons.lang3.math.Fraction
import kotlin.math.max

/**
 * 個数を保持するインターフェース
 */
sealed interface HTAmountView<N> where N : Number, N : Comparable<N> {
    /**
     * このビューが保持している個数を返します。
     */
    fun getAmount(): N

    /**
     * このビューの容量を返します。
     */
    fun getCapacity(): N

    /**
     * このビューの空き容量を返します。
     */
    fun getNeeded(): N

    /**
     * このビューの占有率を返します。
     * @return [Fraction]値での占有率
     */
    fun getStoredLevel(): Fraction

    /**
     * [Int]値を扱う[HTAmountView]の拡張インターフェース
     */
    interface IntSized : HTAmountView<Int> {
        override fun getNeeded(): Int = max(0, getCapacity() - getAmount())

        override fun getStoredLevel(): Fraction = fixedFraction(getAmount(), getCapacity())
    }

    /**
     * [Long]値を扱う[HTAmountView]の拡張インターフェース
     */
    interface LongSized : HTAmountView<Long> {
        fun getAmountAsInt(): Int = Ints.saturatedCast(getAmount())

        fun getCapacityAsInt(): Int = Ints.saturatedCast(getCapacity())

        override fun getNeeded(): Long = max(0, getCapacity() - getAmount())

        override fun getStoredLevel(): Fraction = fixedFraction(getAmount(), getCapacity())
    }
}
