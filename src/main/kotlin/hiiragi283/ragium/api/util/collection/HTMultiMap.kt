package hiiragi283.ragium.api.util.collection

import com.google.common.collect.Multimap

interface HTMultiMap<K : Any, V : Any> {
    val size: Int

    val isEmpty: Boolean

    fun containsKey(key: K): Boolean

    fun containsValue(value: V): Boolean

    operator fun get(key: K): Collection<V>

    val keys: Set<K>

    val values: Collection<V>

    val entries: Set<Pair<K, V>>

    val map: Map<K, Collection<V>>

    interface Mutable<K : Any, V : Any> : HTMultiMap<K, V> {
        fun put(key: K, value: V): Boolean

        fun putAll(key: K, values: Iterable<V>): Boolean

        fun putAll(other: Multimap<K, V>): Boolean

        fun replaceValues(key: K, values: Iterable<V>): Collection<V>

        fun remove(key: K, value: V): Boolean

        fun removeAll(key: K): Collection<V>

        fun clear()
    }
}
