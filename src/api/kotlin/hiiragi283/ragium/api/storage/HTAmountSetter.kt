package hiiragi283.ragium.api.storage

/**
 * 数量を受け取る関数型インターフェース
 */
sealed interface HTAmountSetter<N> where N : Number, N : Comparable<N> {
    fun setAmount(amount: N)

    /**
     * [Int]値を扱う[HTAmountSetter]の拡張インターフェース
     */
    fun interface IntSized : HTAmountSetter<Int>

    /**
     * [Long]値を扱う[HTAmountSetter]の拡張インターフェース
     */
    fun interface LongSized : HTAmountSetter<Long>
}
