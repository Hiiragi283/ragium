package hiiragi283.ragium.api.extension

import java.util.*

fun <T : Any> Optional<T>.toResult(message: String): Result<T> = toResult(NoSuchElementException(message))

fun <T : Any> Optional<T>.toResult(throwable: Throwable): Result<T> = map(Result.Companion::success).orElse(Result.failure(throwable))
