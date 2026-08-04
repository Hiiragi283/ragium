@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 二つの値のうち，その両方または片方だけを保持するクラスです。
 *
 * 参照 : [Arrow - Ior](https://github.com/arrow-kt/arrow/blob/main/arrow-libs/core/arrow-core/src/commonMain/kotlin/arrow/core/Ior.kt)
 * @param A 左側の値のクラス
 * @param B 右側の値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
sealed class Ior<out A, out B> {
    companion object {
        /**
         * 指定された[pair]を[Ior]に変換します。
         */
        @JvmStatic
        fun <A, B> fromNullable(pair: Pair<A?, B?>?): Ior<A, B>? {
            val (first: A?, second: B?) = pair ?: return null
            return fromNullable(first, second)
        }

        /**
         * 指定された[left]と[right]を[Ior]に変換します。
         */
        @JvmStatic
        fun <A, B> fromNullable(left: A?, right: B?): Ior<A, B>? = when {
            left != null -> when {
                right != null -> Both(left, right)
                else -> Left(left)
            }
            else -> when {
                right != null -> Right(right)
                else -> null
            }
        }
    }

    /**
     * このインスタンスが[Left]であるか判定します。
     * @return [Left]の場合は`true`，それ以外の場合は`false`
     */
    fun isLeft(): Boolean = this is Left<A>

    /**
     * このインスタンスが[Right]であるか判定します。
     * @return [Right]の場合は`true`，それ以外の場合は`false`
     */
    fun isRight(): Boolean = this is Right<B>

    /**
     * このインスタンスが[Both]であるか判定します。
     * @return [Both]の場合は`true`，それ以外の場合は`false`
     */
    fun isBoth(): Boolean = this is Both<A, B>

    /**
     * 保持している値を変換します。
     * @param C 変換後のクラス
     * @param left このインスタンスが[Left]の場合の変換ブロック
     * @param right このインスタンスが[Right]の場合の変換ブロック
     * @param both このインスタンスが[Both]の場合の変換ブロック
     * @return 変換された値
     */
    inline fun <C> fold(left: (A) -> C, right: (B) -> C, both: (A, B) -> C): C {
        contract {
            callsInPlace(left, InvocationKind.AT_MOST_ONCE)
            callsInPlace(right, InvocationKind.AT_MOST_ONCE)
            callsInPlace(both, InvocationKind.AT_MOST_ONCE)
        }
        return when (this) {
            is Both -> both(leftValue, rightValue)
            is Left -> left(value)
            is Right -> right(value)
        }
    }

    /**
     * 保持している値を変換します。
     * @param C 変換後のクラス
     * @param left このインスタンスが[Left]の場合の変換ブロック
     * @param right このインスタンスが[Right]の場合の変換ブロック
     * @return 変換された値
     */
    inline fun <C> map(left: (A) -> C, right: (B) -> C): C {
        contract {
            callsInPlace(left, InvocationKind.AT_MOST_ONCE)
            callsInPlace(right, InvocationKind.AT_MOST_ONCE)
        }
        return when (this) {
            is Both -> right(rightValue)
            is Left -> left(value)
            is Right -> right(value)
        }
    }

    /**
     * 保持している値を変換します。
     * @param C 変換後のクラス
     * @param right このインスタンスが[Right]または[Both]の場合の変換ブロック
     * @return 変換された[Ior]のインスタンス
     */
    inline fun <C> mapRight(right: (B) -> C): Ior<A, C> {
        contract {
            callsInPlace(right, InvocationKind.AT_MOST_ONCE)
        }
        return when (this) {
            is Both -> Both(leftValue, right(rightValue))
            is Left -> Left(value)
            is Right -> Right(right(value))
        }
    }

    /**
     * 保持している値を変換します。
     * @param C 変換後のクラス
     * @param left このインスタンスが[Left]または[Both]の場合の変換ブロック
     * @return 変換された[Ior]のインスタンス
     */
    inline fun <C> mapLeft(left: (A) -> C): Ior<C, B> {
        contract {
            callsInPlace(left, InvocationKind.AT_MOST_ONCE)
        }
        return when (this) {
            is Both -> Both(left(leftValue), rightValue)
            is Left -> Left(left(value))
            is Right -> Right(value)
        }
    }

    /**
     * 保持している値を入れ替えます。
     * @return 値が入れ替わった[Ior]のインスタンス
     */
    fun swap(): Ior<B, A> = fold(
        { Right(it) },
        { Left(it) },
        { left: A, right: B -> Both(right, left) },
    )

    /**
     * 保持している値を[Either]に展開します。
     * @return 展開された[Either]のインスタンス
     */
    fun unwrap(): Either<Either<A, B>, Pair<A, B>> = fold(
        { Either.Left(Either.Left(it)) },
        { Either.Left(Either.Right(it)) },
        { left: A, right: B -> Either.Right(left to right) },
    )

    /**
     * 保持している値を[Pair]に展開します。
     * @return 展開された[Pair]のインスタンス
     */
    fun toPair(): Pair<A?, B?> = fold(
        { it to null },
        { null to it },
        { left: A, right: B -> left to right },
    )

    /**
     * [右側][B]の値を取得します。
     * @return [Left]の場合は`null`
     */
    fun getRight(): B? = fold(
        { null },
        identity(),
        { _: A, right: B -> right },
    )

    /**
     * [左側][A]の値を取得します。
     * @return [Right]の場合は`null`
     */
    fun getLeft(): A? = fold(
        identity(),
        { null },
        { left: A, _: B -> left },
    )

    /**
     * [A]だけを保持する[Ior]の実装クラスです。
     */
    data class Left<A>(val value: A) : Ior<A, Nothing>()

    /**
     * [B]だけを保持する[Ior]の実装クラスです。
     */
    data class Right<B>(val value: B) : Ior<Nothing, B>()

    /**
     * [A]と[B]の両方を保持する[Ior]の実装クラスです。
     */
    data class Both<A, B>(val leftValue: A, val rightValue: B) : Ior<A, B>()
}
