package hiiragi283.lib.collection

/**
 * [Set]に基づいた[MultiMap]の実装クラスです。
 * @param K キーのクラス
 * @param V 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class SetMultiMap<K, out V> private constructor(map: Map<K, Set<V>>) : AbstractMultiMap<K, V, Set<V>>(map) {
    companion object {
        /**
         * 指定した[map]を[MultiMap]に変換します。
         * @return [map]が空の場合は[emptyMultiMapOf]
         */
        @JvmStatic
        fun <K, V> copyOf(map: Map<K, Set<V>>): MultiMap<K, V> = when {
            map.isDeepEmpty() -> emptyMultiMapOf()
            else -> SetMultiMap(map)
        }
    }

    override fun emptyCollection(): Set<V> = setOf()

    /**
     * [MutableSet]に基づいた[MultiMap.Builder]の抽象クラスです。
     * @param K キーのクラス
     * @param V 値のクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.1
     */
    class Builder<K, V> : AbstractMultiMap.Builder<K, V, MutableSet<V>> {
        constructor(map: MutableMap<K, MutableSet<V>>) : super(map)

        constructor(initialCapacity: Int = 10) : super(initialCapacity)

        constructor(other: MultiMap<K, V>) : super(other)

        override fun emptyCollection(): MutableSet<V> = mutableSetOf()

        override fun build(): MultiMap<K, V> = when {
            map.isDeepEmpty() -> emptyMultiMapOf()
            else -> SetMultiMap(map)
        }
    }
}
