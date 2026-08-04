package hiiragi283.lib.collection

import hiiragi283.lib.util.Either
import hiiragi283.lib.util.Option
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

//    Optional    //

fun <K, V : Any> Map<K, Optional<out V>>.filterNotOptional(): Map<K, V> = this.filterNotOptionalTo(mutableMapOf())

fun <K, V : Any, C : MutableMap<K, V>> Map<K, Optional<out V>>.filterNotOptionalTo(destination: C): C {
    for ((key: K, value: Optional<out V>) in this) {
        val valueIn: V = value.getOrNull() ?: continue
        destination[key] = valueIn
    }
    return destination
}

//    Option    //

fun <K, V : Any> Map<K, Option<V>>.filterNotOption(): Map<K, V> = this.filterNotOptionTo(mutableMapOf())

fun <K, V : Any, C : MutableMap<K, V>> Map<K, Option<V>>.filterNotOptionTo(destination: C): C {
    for ((key: K, value: Option<V>) in this) {
        val valueIn: V = value.getOrNull() ?: continue
        destination[key] = valueIn
    }
    return destination
}

//    Either    //

fun <K, A, B> Map<K, Either<A, B>>.separateEither(): Pair<Map<K, A>, Map<K, B>> = this.separateEitherTo(mutableMapOf(), mutableMapOf())

fun <K, A, B, CA : MutableMap<K, in A>, CB : MutableMap<K, in B>> Map<K, Either<A, B>>.separateEitherTo(destinationA: CA, destinationB: CB): Pair<CA, CB> {
    for ((key: K, value: Either<A, B>) in this) {
        value.fold({ destinationA[key] = it }, { destinationB[key] = it })
    }
    return destinationA to destinationB
}

//    MultiMap    //

@Suppress("NOTHING_TO_INLINE")
@JvmName("toListMultiMap")
inline fun <K, V> Map<K, List<V>>.toMultiMap(): MultiMap<K, V> = ListMultiMap.copyOf(this)

@JvmName("mapListMultiMapTo")
inline fun <K, V, W> Map<K, V>.mapMultiMapTo(transform: (Map.Entry<K, V>) -> List<W>): MultiMap<K, W> = this.mapValues(transform).toMultiMap()

@Suppress("NOTHING_TO_INLINE")
@JvmName("toSetMultiMap")
inline fun <K, V> Map<K, Set<V>>.toMultiMap(): MultiMap<K, V> = SetMultiMap.copyOf(this)

@JvmName("mapSetMultiMapTo")
inline fun <K, V, W> Map<K, V>.mapMultiMapTo(transform: (Map.Entry<K, V>) -> Set<W>): MultiMap<K, W> = this.mapValues(transform).toMultiMap()

//    Table    //

inline fun <K, V, R, C, W> Map<K, V>.mapTable(transform: (Map.Entry<K, V>) -> Triple<R, C, W>): Table<R, C, W> = this.mapTableTo(PairMapTable.Builder(), transform)

inline fun <K, V, R, C, W, D : Table.Builder<R, C, W>> Map<K, V>.mapTableTo(builder: D, transform: (Map.Entry<K, V>) -> Triple<R, C, W>): Table<R, C, W> {
    this.entries.map(transform).forEach(builder::put)
    return builder.build()
}
