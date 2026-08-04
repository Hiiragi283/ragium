package hiiragi283.lib.collection

/**
 * 一つのキーに対して複数の値が対応するコレクションを表すインターフェースです。
 *
 * 参照 : [Google Guava - Multimap][com.google.common.collect.Multimap]
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface MultiMap<K, out V> {
    /**
     * 組の個数
     */
    val size: Int

    /**
     * このマップが空かどうか判定します。
     */
    val isEmpty: Boolean

    /**
     * 指定したキーが含まれているか判定します。
     */
    fun containsKey(key: K): Boolean

    operator fun contains(key: K): Boolean = containsKey(key)

    /**
     * 指定した値が含まれているか判定します。
     */
    fun containsValue(value: @UnsafeVariance V): Boolean

    /**
     * 指定したキーから値の一覧を取得します。
     */
    operator fun get(key: K): Collection<V>

    /**
     * キーの一覧
     */
    val keys: Set<K>

    /**
     * 値の一覧
     */
    val values: Collection<V>

    /**
     * 組の一覧
     */
    val entries: Set<Map.Entry<K, Collection<V>>>

    /**
     * 値ごとに分割した組の一覧
     */
    val flatEntries: Set<Pair<K, V>> get() = entries.flatMapTo(mutableSetOf()) { (key: K, values: Collection<V>) -> values.map { key to it } }

    /**
     * [Map]に変換します。
     */
    fun asMap(): Map<K, Collection<V>>

    /**
     * [MultiMap]のビルダーを表すインターフェースです。
     * @param K キーのクラス
     * @param V 値のクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.1
     */
    interface Builder<K, V> {
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
         * [MultiMap]を生成します。
         */
        fun build(): MultiMap<K, V>
    }
}
