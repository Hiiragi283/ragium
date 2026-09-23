package hiiragi283.lib.collection

import hiiragi283.lib.util.Either
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

//    Either    //

fun <K, A, B> Map<K, Either<A, B>>.separateEither(): Pair<Map<K, A>, Map<K, B>> =
    this.separateEitherTo(mutableMapOf(), mutableMapOf())

fun <K, A, B, CA : MutableMap<K, in A>, CB : MutableMap<K, in B>> Map<K, Either<A, B>>.separateEitherTo(
    destinationA: CA,
    destinationB: CB
): Pair<CA, CB> {
    for ((key: K, value: Either<A, B>) in this) {
        value.fold({ destinationA[key] = it }, { destinationB[key] = it })
    }
    return destinationA to destinationB
}
