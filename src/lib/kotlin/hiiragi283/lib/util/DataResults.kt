package hiiragi283.lib.util

import com.mojang.serialization.DataResult
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
fun <R : Any> R.dataResult(): DataResult<R> = DataResult.success(this)

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
fun <R : Any> R?.dataResult(errorMessage: () -> String): DataResult<R> = this?.dataResult() ?: DataResult.error(errorMessage)

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
fun <R : Any> DataResult<R>.getOrNull(): R? = this.result().getOrNull()

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
inline fun <R : Any> DataResult<R>.getOrElse(defaultValue: () -> R): R = this.result().getOrElse(defaultValue)

//    DataResult <-> Either    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
fun <R : Any> DataResult<R>.toEither(): Either<String, R> = when (this) {
    is DataResult.Error<R> -> Either.Left(this.message())
    is DataResult.Success<R> -> Either.Right(this.value)
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
fun <R : Any> Either<String, R>.toDataResult(): DataResult<R> = this.fold({ DataResult.error { it } }, { it.dataResult() })

//    DataResult <-> Ior    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
fun <R : Any> DataResult<R>.toIor(): Ior<String, R> = Ior.fromNullable(this.error().map { it.message() }, this.result()).orElseThrow()

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
fun <R : Any> Ior<String, R>.toDataResult(): DataResult<R> = this.fold(
    { DataResult.error { it } },
    { it.dataResult() },
    { error: String, partialResult: R -> DataResult.error({ error }, partialResult) },
)
