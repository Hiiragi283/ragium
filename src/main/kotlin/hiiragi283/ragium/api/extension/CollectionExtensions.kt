package hiiragi283.ragium.api.extension

import com.google.common.collect.HashBasedTable
import com.google.common.collect.HashMultimap
import com.google.common.collect.ImmutableMultimap
import hiiragi283.ragium.api.util.collection.HTMultiMap
import hiiragi283.ragium.api.util.collection.HTTable
import hiiragi283.ragium.api.util.collection.HTWrappedMultiMap
import hiiragi283.ragium.api.util.collection.HTWrappedTable

//    MultiMap    //

fun <K : Any, V : Any> multiMapOf(vararg pairs: Pair<K, V>): HTMultiMap<K, V> = HTWrappedMultiMap(
    ImmutableMultimap.builder<K, V>().apply { pairs.forEach { (key: K, value: V) -> put(key, value) } }.build(),
)

fun <K : Any, V : Any> mutableMultiMapOf(vararg pairs: Pair<K, V>): HTMultiMap.Mutable<K, V> = HTWrappedMultiMap.Mutable(
    HashMultimap.create<K, V>().apply {
        pairs.forEach { (key: K, value: V) -> put(key, value) }
    },
)

fun <K : Any, V : Any> buildMultiMap(builderAction: HTMultiMap.Mutable<K, V>.() -> Unit): HTMultiMap<K, V> =
    mutableMultiMapOf<K, V>().apply(builderAction)

fun <K : Any, V : Any> HTMultiMap<K, V>.forEach(action: (K, V) -> Unit) {
    entries.forEach { (k: K, v: V) -> action(k, v) }
}

fun <K : Any, V : Any> Map<out K, Iterable<V>>.toMultiMap(): HTMultiMap<K, V> = buildMultiMap {
    this@toMultiMap.forEach { (key: K, values: Iterable<V>) -> values.forEach { put(key, it) } }
}

//    Table    //

fun <R : Any, C : Any, V : Any> tableOf(): HTTable<R, C, V> = HTWrappedTable(HashBasedTable.create())

fun <R : Any, C : Any, V : Any> mutableTableOf(): HTWrappedTable.Mutable<R, C, V> = HTWrappedTable.Mutable(HashBasedTable.create())

fun <R : Any, C : Any, V : Any> buildTable(builderAction: HTTable.Mutable<R, C, V>.() -> Unit): HTTable<R, C, V> =
    HTWrappedTable.Mutable(HashBasedTable.create<R, C, V>()).apply(builderAction)

fun <R : Any, C : Any, V : Any> HTTable<R, C, V>.forEach(action: (Triple<R, C, V>) -> Unit) {
    entries.forEach(action)
}

fun <R : Any, C : Any, V : Any> HTTable.Mutable<R, C, V>.computeIfAbsent(row: R, column: C, mapper: (R, C) -> V): V {
    val value: V? = get(row, column)
    if (value == null) {
        val newValue: V = mapper(row, column)
        put(row, column, newValue)
        return newValue
    }
    return value
}

fun <R : Any, C : Any, V : Any> HTTable<R, C, V>.asPairMap(): Map<Pair<R, C>, V> =
    entries.associate { (row: R, column: C, value: V) -> (row to column) to value }

fun <R : Any, C : Any, V : Any> Map<Pair<R, C>, V>.toTable(): HTTable<R, C, V> = buildTable {
    this@toTable.forEach { (pair: Pair<R, C>, value: V) ->
        put(pair.first, pair.second, value)
    }
}
