package hiiragi283.ragium.api.extension

import com.google.common.collect.HashBasedTable
import com.google.common.collect.HashMultimap
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.collection.HTMultiMap
import hiiragi283.ragium.api.collection.HTTable
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.NonNullList
import net.minecraft.util.RandomSource
import kotlin.random.Random

//    MultiMap    //

fun <K : Any, V : Any> multiMapOf(): HTMultiMap.Mutable<K, V> = RagiumAPI.INSTANCE.createMultiMap(HashMultimap.create())

inline fun <K : Any, V : Any> buildMultiMap(builderAction: HTMultiMap.Mutable<K, V>.() -> Unit): HTMultiMap<K, V> =
    multiMapOf<K, V>().apply(builderAction)

inline fun <K : Any, V : Any> HTMultiMap<K, V>.forEach(action: (K, V) -> Unit) {
    for ((k: K, v: V) in entries) {
        action(k, v)
    }
}

//    Table    //

fun <R : Any, C : Any, V : Any> mutableTableOf(): HTTable.Mutable<R, C, V> = RagiumAPI.INSTANCE.createTable(HashBasedTable.create())

inline fun <R : Any, C : Any, V : Any> buildTable(builderAction: HTTable.Mutable<R, C, V>.() -> Unit): HTTable<R, C, V> =
    mutableTableOf<R, C, V>().apply(builderAction)

fun <R : Any, C : Any, V : Any> Map<C, V>.toRowTableBy(rowKey: R): HTTable<R, C, V> = buildTable {
    for ((column: C, value: V) in this@toRowTableBy) {
        put(rowKey, column, value)
    }
}

fun <R : Any, C : Any, V : Any> Map<R, V>.toColumnTableBy(columnKey: C): HTTable<R, C, V> = buildTable {
    for ((row: R, value: V) in this@toColumnTableBy) {
        put(row, columnKey, value)
    }
}

inline fun <R : Any, C : Any, V : Any> HTTable<R, C, V>.forEach(action: (Triple<R, C, V>) -> Unit) {
    entries.forEach(action)
}

inline fun <R : Any, C : Any, V : Any> HTTable.Mutable<R, C, V>.computeIfAbsent(row: R, column: C, mapper: (R, C) -> V): V {
    val value: V? = get(row, column)
    if (value == null) {
        val newValue: V = mapper(row, column)
        put(row, column, newValue)
        return newValue
    }
    return value
}

fun <R : Any, C : Any, V : Any> HTTable<R, C, V>.rowValues(row: R): Collection<V> = row(row).values

fun <R : Any, C : Any, V : Any> HTTable<R, C, V>.columnValues(column: C): Collection<V> = column(column).values

//    NonNullList    //

fun <T : Any> Collection<T>.toNonNullList(): NonNullList<T> = NonNullList.copyOf(this)

//    RandomSource    //

fun RandomSource.asKotlinRandom(): Random = RagiumAPI.INSTANCE.wrapRandom(this)

//    HolderSet    //

fun <T : Any> HolderSet<T>.asList(): List<Holder<T>> = RagiumAPI.INSTANCE.wrapHolderSet(this)
