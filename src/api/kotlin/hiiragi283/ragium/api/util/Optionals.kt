package hiiragi283.ragium.api.util

import java.util.Optional

fun <T : Any> T?.wrapOptional(): Optional<T> = Optional.ofNullable(this)

fun <T : Any> Optional<T>.toResult(): Result<T> = runCatching { orElseThrow() }

fun <T : Any> Optional<T>.toResult(exceptionSupplier: () -> Exception): Result<T> = runCatching { orElseThrow(exceptionSupplier) }
