package hiiragi283.ragium.api.extension

import com.mojang.serialization.DataResult
import java.util.Optional
import java.util.function.Function
import java.util.function.Predicate
import java.util.function.Supplier
import kotlin.jvm.optionals.getOrNull

//    DataResult    //

fun <T : Any> T?.wrapDataResult(message: String = "Value is null"): DataResult<T> =
    this?.let(DataResult<T>::success) ?: DataResult.error { message }

fun <T : Any> DataResult<T>.resultOrNull(): T? = result().getOrNull()

fun <T : Any> DataResult<T>.filter(filter: Predicate<T>, message: String = "Not matching filter"): DataResult<T> = flatMap { value: T ->
    when (filter.test(value)) {
        true -> DataResult.success(value)
        false -> DataResult.error { message }
    }
}

fun <T : Any> DataResult<T>.filterNot(filter: Predicate<T>, message: String = "Not matching filter"): DataResult<T> =
    filter(filter.negate(), message)

//    Optional    //

fun <T : Any> T?.wrapOptional(): Optional<T> = Optional.ofNullable(this)

fun <T : Any, R : Any> Optional<T>.mapNotNull(function: Function<T, R?>): Optional<R> =
    flatMap { value: T -> Optional.ofNullable(function.apply(value)) }

fun <T : Any> Optional<T>.mapIfEmpty(supplier: Supplier<T?>): Optional<T> = when {
    this.isEmpty -> supplier.get().wrapOptional()
    else -> this
}

fun <T : Any> Optional<T>.wrapDataResult(message: String = "Value is null"): DataResult<T> =
    this.map(DataResult<T>::success).orElseGet { DataResult.error { message } }
