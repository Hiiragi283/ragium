package hiiragi283.ragium.api.util

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.function.identity

sealed class Ior<A, B> {
    fun isLeft(): Boolean = this is Left<A, B>

    fun isRight(): Boolean = this is Right<A, B>

    fun isBoth(): Boolean = this is Both<A, B>

    inline fun <C> fold(left: (A) -> C, right: (B) -> C, both: (A, B) -> C): C = when (this) {
        is Both<A, B> -> both(leftValue, rightValue)
        is Left<A, B> -> left(value)
        is Right<A, B> -> right(value)
    }

    inline fun <C> map(left: (A) -> C, right: (B) -> C): C = when (this) {
        is Both<A, B> -> right(rightValue)
        is Left<A, B> -> left(value)
        is Right<A, B> -> right(value)
    }

    inline fun <C> mapRight(right: (B) -> C): Ior<A, C> = when (this) {
        is Both<A, B> -> Both(leftValue, right(rightValue))
        is Left<A, B> -> Left(value)
        is Right<A, B> -> Right(right(value))
    }

    inline fun <C> mapLeft(left: (A) -> C): Ior<C, B> = when (this) {
        is Both<A, B> -> Both(left(leftValue), rightValue)
        is Left<A, B> -> Left(left(value))
        is Right<A, B> -> Right(value)
    }

    fun swap(): Ior<B, A> = fold(
        { Right(it) },
        { Left(it) },
        { left: A, right: B -> Both(right, left) },
    )

    fun unwrap(): Either<Either<A, B>, Pair<A, B>> = fold(
        { Either.left(Either.left(it)) },
        { Either.left(Either.right(it)) },
        { left: A, right: B -> Either.right(left to right) },
    )

    fun toPair(): Pair<A?, B?> = fold(
        { it to null },
        { null to it },
        { left: A, right: B -> left to right },
    )

    fun getRight(): B? = fold(
        { null },
        identity(),
        { _: A, right: B -> right },
    )

    fun getLeft(): A? = fold(
        identity(),
        { null },
        { left: A, _: B -> left },
    )

    data class Left<A, B>(val value: A) : Ior<A, B>()

    data class Right<A, B>(val value: B) : Ior<A, B>()

    data class Both<A, B>(val leftValue: A, val rightValue: B) : Ior<A, B>()
}
