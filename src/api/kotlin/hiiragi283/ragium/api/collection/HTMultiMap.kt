package hiiragi283.ragium.api.collection

/**
 * @see [com.google.common.collect.Multimap]
 */
interface HTMultiMap<K : Any, V : Any> {
    val size: Int

    val isEmpty: Boolean

    fun containsKey(key: K): Boolean

    operator fun contains(key: K): Boolean = containsKey(key)

    fun containsValue(value: V): Boolean

    operator fun get(key: K): Collection<V>

    val keys: Set<K>

    val values: Collection<V>

    val entries: Set<Pair<K, V>>

    val map: Map<K, Collection<V>>

    interface Mutable<K : Any, V : Any> : HTMultiMap<K, V> {
        fun put(key: K, value: V): Boolean

        operator fun set(key: K, value: V) {
            put(key, value)
        }

        fun putAll(key: K, values: Iterable<V>): Boolean

        fun putAll(other: HTMultiMap<K, V>): Boolean

        fun replaceValues(key: K, values: Iterable<V>): Collection<V>

        fun remove(key: K, value: V): Boolean

        fun removeAll(key: K): Collection<V>

        fun clear()
    }
}
