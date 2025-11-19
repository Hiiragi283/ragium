package hiiragi283.ragium.api.collection

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap

/**
 * [Multimap]のイミュータブルなラッパー
 */
@JvmInline
value class ImmutableMultiMap<K : Any, V : Any>(private val multimap: Multimap<K, V>) {
    val size: Int get() = multimap.size()

    fun isEmpty(): Boolean = multimap.isEmpty

    fun containsKey(key: K): Boolean = multimap.containsKey(key)

    operator fun contains(key: K): Boolean = containsKey(key)

    fun containsValue(value: V): Boolean = multimap.containsValue(value)

    operator fun get(key: K): Collection<V> = multimap.get(key)

    val keys: Set<K> get() = multimap.keySet()

    val values: Collection<V> get() = multimap.values()

    val entries: Collection<Map.Entry<K, V>> get() = multimap.entries()

    val map: Map<K, Collection<V>> get() = multimap.asMap()

    //    Extensions    //

    inline fun forEach(action: (Map.Entry<K, V>) -> Unit) {
        entries.forEach(action)
    }

    //    Builder    //

    class Builder<K : Any, V : Any>(initialCapacity: Int = 10, perKey: Int = 2) {
        private val values: Multimap<K, V> = HashMultimap.create<K, V>(initialCapacity, perKey)

        fun put(key: K, value: V): Builder<K, V> {
            values.put(key, value)
            return this
        }

        operator fun set(key: K, value: V) {
            put(key, value)
        }

        fun putAll(key: K, values: Iterable<V>): Builder<K, V> {
            this.values.putAll(key, values)
            return this
        }

        fun putAll(key: K, vararg values: V): Builder<K, V> {
            for (value: V in values) {
                put(key, value)
            }
            return this
        }

        fun putAll(map: Map<K, V>): Builder<K, V> {
            map.forEach { (key: K, value: V) -> this.values.put(key, value) }
            return this
        }

        fun putAll(multiMap: ImmutableMultiMap<K, V>): Builder<K, V> {
            multiMap.forEach { (key: K, value: V) -> this.values.put(key, value) }
            return this
        }

        fun replaceValues(key: K, values: Iterable<V>): Builder<K, V> {
            this.values.putAll(key, values)
            return this
        }

        fun build(): ImmutableMultiMap<K, V> = ImmutableMultiMap(values)
    }
}
