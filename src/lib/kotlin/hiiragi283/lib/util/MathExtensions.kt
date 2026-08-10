package hiiragi283.lib.util

/**
 * 指定した引数から除算を計算します。
 * @param amount 分子となる値
 * @param capacity 分母となる値
 * @param loop `true`の場合は[amount]を[capacity]で割った剰余を，`false`の場合は[capacity]以下の値を分子に使用します
 */
fun fixedFraction(amount: Int, capacity: Int, loop: Boolean = false): Float {
    if (capacity <= 0) return 0f
    val fixedAmount: Int = when (loop) {
        true -> amount % capacity
        false -> minOf(amount, capacity)
    }
    return when (fixedAmount) {
        capacity -> 1f
        else -> fixedAmount.toFloat() / capacity
    }
}

/**
 * 指定した引数から除算を計算します。
 * @param amount 分子となる値
 * @param capacity 分母となる値
 * @param loop `true`の場合は[amount]を[capacity]で割った剰余を，`false`の場合は[capacity]以下の値を分子に使用します
 */
fun fixedFraction(amount: Long, capacity: Long, loop: Boolean = false): Double {
    if (capacity <= 0) return 0.0
    val fixedAmount: Long = when (loop) {
        true -> amount % capacity
        false -> minOf(amount, capacity)
    }
    return when (fixedAmount) {
        capacity -> 1.0
        else -> fixedAmount.toDouble() / capacity
    }
}
