package hiiragi283.ragium.api.storage

import com.google.common.primitives.Ints

/**
 * 個数を保持するインターフェース
 */
sealed interface HTAmountView<N : Number> {
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
     * @return [Double]値での占有率
     */
    fun getStoredLevelAsDouble(): Double

    /**
     * このビューの占有率を返します。
     * @return [Float]値での占有率
     */
    fun getStoredLevelAsFloat(): Float

    /**
     * [Int]値を扱う[HTAmountView]の拡張インターフェース
     */
    interface IntSized : HTAmountView<Int> {
        override fun getNeeded(): Int = getCapacity() - getAmount()

        override fun getStoredLevelAsDouble(): Double = getAmount() / getCapacity().toDouble()

        override fun getStoredLevelAsFloat(): Float = getAmount() / getCapacity().toFloat()
    }

    /**
     * [Long]値を扱う[HTAmountView]の拡張インターフェース
     */
    interface LongSized : HTAmountView<Long> {
        fun getAmountAsInt(): Int = Ints.saturatedCast(getAmount())

        fun getCapacityAsInt(): Int = Ints.saturatedCast(getCapacity())

        override fun getNeeded(): Long = getCapacity() - getAmount()

        override fun getStoredLevelAsDouble(): Double = getAmount() / getCapacity().toDouble()

        override fun getStoredLevelAsFloat(): Float = getAmount() / getCapacity().toFloat()
    }
}
