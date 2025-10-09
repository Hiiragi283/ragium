package hiiragi283.ragium.api.extension

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.data.DataResultException
import java.util.Optional

//    Optional    //

fun <T : Any> T?.wrapOptional(): Optional<T> = Optional.ofNullable(this)

//    Throwable    //

@Suppress("NOTHING_TO_INLINE")
inline fun unsupported(): Nothing = throw UnsupportedOperationException()

@Suppress("NOTHING_TO_INLINE")
inline fun unsupported(message: String): Nothing = throw UnsupportedOperationException(message)

//    Result    //

fun <T : Any> Optional<T>.toResult(): Result<T> = runCatching { orElseThrow() }

fun <T : Any> Optional<T>.toResult(exceptionSupplier: () -> Exception): Result<T> = runCatching { orElseThrow(exceptionSupplier) }

fun <T : Any> DataResult<T>.toResult(exceptionSupplier: (String) -> Exception = ::DataResultException): Result<T> =
    mapOrElse(Result.Companion::success, DataResult.Error<T>::message.andThen(exceptionSupplier).andThen(Result.Companion::failure))

fun <T : Any> Result<T>.toData(): DataResult<T> = fold(DataResult<T>::success) { throwable: Throwable ->
    DataResult.error { throwable.message ?: "Thrown exception" }
}

internal fun <T : Any> resultToData(): (Result<T>) -> DataResult<T> = Result<T>::toData

fun <T : Any, R : Any> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> = mapCatching { transform(it).getOrThrow() }
