package hiiragi283.ragium.api.extension

import com.google.common.collect.HashBasedTable
import com.google.common.collect.HashMultimap
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.HTMultiMap
import hiiragi283.ragium.api.util.HTTable

//    MultiMap    //

fun <K : Any, V : Any> buildMultiMap(builderAction: HTMultiMap.Mutable<K, V>.() -> Unit): HTMultiMap<K, V> =
    RagiumAPI.getInstance().createMultiMap<K, V>(HashMultimap.create()).apply(builderAction)

fun <K : Any, V : Any> HTMultiMap<K, V>.forEach(action: (K, V) -> Unit) {
    entries.forEach { (k: K, v: V) -> action(k, v) }
}

//    Table    //

fun <R : Any, C : Any, V : Any> mutableTableOf(): HTTable.Mutable<R, C, V> =
    RagiumAPI.getInstance().createTable<R, C, V>(HashBasedTable.create())

fun <R : Any, C : Any, V : Any> buildTable(builderAction: HTTable.Mutable<R, C, V>.() -> Unit): HTTable<R, C, V> =
    mutableTableOf<R, C, V>().apply(builderAction)

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
