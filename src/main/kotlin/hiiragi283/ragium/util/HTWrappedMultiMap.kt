package hiiragi283.ragium.util

import com.google.common.collect.Multimap
import hiiragi283.ragium.api.util.HTMultiMap

internal open class HTWrappedMultiMap<K : Any, V : Any>(protected val delegated: Multimap<K, V>) : HTMultiMap<K, V> {
    override val size: Int get() = delegated.size()
    override val isEmpty: Boolean get() = delegated.isEmpty

    override fun containsKey(key: K): Boolean = delegated.containsKey(key)

    override fun containsValue(value: V): Boolean = delegated.containsValue(value)

    override fun get(key: K): Collection<V> = delegated[key]

    override val keys: Set<K> get() = delegated.keySet()
    override val values: Collection<V> get() = delegated.values()
    override val entries: Set<Pair<K, V>> get() = delegated.entries().map { (key: K, value: V) -> key to value }.toSet()
    override val map: Map<K, Collection<V>> get() = delegated.asMap()

    //    Mutable    //

    class Mutable<K : Any, V : Any>(delegated: Multimap<K, V>) :
        HTWrappedMultiMap<K, V>(delegated),
        HTMultiMap.Mutable<K, V> {
        override fun put(key: K, value: V): Boolean = delegated.put(key, value)

        override fun putAll(key: K, values: Iterable<V>): Boolean = delegated.putAll(key, values)

        override fun putAll(other: Multimap<K, V>): Boolean = delegated.putAll(other)

        override fun replaceValues(key: K, values: Iterable<V>): Collection<V> = delegated.replaceValues(key, values)

        override fun remove(key: K, value: V): Boolean = delegated.remove(key, value)

        override fun removeAll(key: K): Collection<V> = delegated.removeAll(key)

        override fun clear() {
            delegated.clear()
        }
    }
}
