package hiiragi283.lib.collection

/**
 * 値に[Collection]を持つ[Map]のビルダーを表すインターフェースです。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
interface MultiMapBuilder<K, V> {
    /**
     * 値を追加します。
     */
    fun put(key: K, value: V): Boolean

    operator fun set(key: K, value: V) {
        put(key, value)
    }

    /**
     * 値を追加します。
     */
    fun putAll(key: K, values: Iterable<V>): Boolean

    /**
     * 値を追加します。
     */
    fun putAll(key: K, vararg values: V): Boolean

    /**
     * 値を追加します。
     */
    fun putAll(key: K, values: Sequence<V>): Boolean

    /**
     * @since 26.1.2
     */
    fun remove(key: K, value: V): Boolean

    /**
     * @since 26.1.2
     */
    fun removeAll(key: K): Collection<V>?

    /**
     * [Map]を生成します。
     */
    fun build(): Map<K, Collection<V>>
}
