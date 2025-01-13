package hiiragi283.ragium.api.property

/**
 * さまざまな型の値を保持するインターフェース
 *
 * シリアライズ不可能な[net.minecraft.component.ComponentHolder]
 *
 * @see [HTMutablePropertyHolder]
 */
interface HTPropertyHolder : Iterable<Pair<HTPropertyKey<*>, Any>> {
    /**
     * 指定された[key]から[T]を返します。
     * @return [key]に紐づいた値がない場合はnull
     */
    operator fun <T : Any> get(key: HTPropertyKey<T>): T?

    /**
     * 指定された[key]から[T]を返します。
     * @return [key]に紐づいた値がない場合は[HTPropertyKey.Defaulted.getDefaultValue]
     */
    fun <T : Any> getOrDefault(key: HTPropertyKey.Defaulted<T>): T = get(key) ?: key.getDefaultValue()

    /**
     * 指定された[key]から[T]を返します。
     * @throws IllegalStateException [key]に紐づいた値がない場合
     */
    fun <T : Any> getOrThrow(key: HTPropertyKey<T>): T = get(key) ?: error("Unknown property key: $key")

    /**
     * 指定された[key]が含まれているか判定します。
     */
    operator fun contains(key: HTPropertyKey<*>): Boolean

    /**
     * 指定された[key]に紐づいた値を[transform]で変換します。
     * @return [key]に紐づいた値がない場合はnull
     */
    fun <T : Any, R : Any> map(
        key: HTPropertyKey<T>,
        transform: (T) -> R,
    ): R? = get(key)?.let(transform)

    /**
     * 指定された[key]に紐づいた値を[action]に渡します。
     * @return [key]に紐づいた値がない場合は実行されない
     */
    fun <T : Any> ifPresent(
        key: HTPropertyKey<T>,
        action: (T) -> Unit,
    ) {
        map(key, action)
    }

    //    Empty    //

    /**
     * 空で不変な[HTPropertyHolder]の実装
     */
    object Empty : HTPropertyHolder {
        override fun <T : Any> get(key: HTPropertyKey<T>): T? = null

        override fun contains(key: HTPropertyKey<*>): Boolean = false

        override fun iterator(): Iterator<Pair<HTPropertyKey<*>, Any>> = listOf<Pair<HTPropertyKey<*>, Any>>().iterator()
    }
}
