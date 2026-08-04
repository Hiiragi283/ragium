package hiiragi283.lib.collection

import hiiragi283.lib.util.Either
import hiiragi283.lib.util.Option
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

//    Optional    //

inline fun <T, R : Any> Iterable<T>.mapOptional(transform: (T) -> Optional<out R>): List<R> = this.mapOptionalTo(mutableListOf(), transform)

inline fun <T, R : Any, C : MutableCollection<in R>> Iterable<T>.mapOptionalTo(destination: C, transform: (T) -> Optional<out R>): C = this.mapNotNullTo(destination) { transform(it).getOrNull() }

fun <T : Any> Iterable<Optional<out T>>.filterNotOptional(): List<T> = this.filterNotOptionalTo(mutableListOf())

fun <T : Any, C : MutableCollection<in T>> Iterable<Optional<out T>>.filterNotOptionalTo(destination: C): C = this.mapNotNullTo(destination) { it.getOrNull() }

//    Option    //

inline fun <T, R : Any> Iterable<T>.mapOption(transform: (T) -> Option<R>): List<R> = this.mapOptionTo(mutableListOf(), transform)

inline fun <T, R : Any, C : MutableCollection<in R>> Iterable<T>.mapOptionTo(destination: C, transform: (T) -> Option<R>): C = this.mapNotNullTo(destination) { transform(it).getOrNull() }

fun <T : Any> Iterable<Option<T>>.filterNotOption(): List<T> = this.filterNotOptionTo(mutableListOf())

fun <T : Any, C : MutableCollection<in T>> Iterable<Option<T>>.filterNotOptionTo(destination: C): C = this.mapNotNullTo(destination) { it.getOrNull() }

//    Either    //

fun <A, B> Iterable<Either<A, B>>.separateEither(): Pair<List<A>, List<B>> = this.separateEitherTo(mutableListOf(), mutableListOf())

fun <A, B, CA : MutableCollection<in A>, CB : MutableCollection<in B>> Iterable<Either<A, B>>.separateEitherTo(destinationA: CA, destinationB: CB): Pair<CA, CB> = this.mapNotNullTo(destinationA) { it.leftOrNull() } to this.mapNotNullTo(destinationB) { it.getOrNull() }
