@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 二つの値のうち，片方だけを保持するクラスです。
 *
 * 参照 : [Arrow - Either](https://github.com/arrow-kt/arrow/blob/main/arrow-libs/core/arrow-core/src/commonMain/kotlin/arrow/core/Either.kt)
 * @param A 左側の値のクラス
 * @param B 右側の値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
sealed class Either<out A, out B> {
    /**
     * 左側の値があるかどうか判定します。
     * @return 値がある場合は`true`
     */
    fun isLeft(): Boolean = this is Left<*>

    /**
     * 右側の値があるかどうか判定します。
     * @return 値がある場合は`true`
     */
    fun isRight(): Boolean = this is Right<*>

    /**
     * 左側の値を取得します。
     * @return 値がない場合は`null`
     */
    fun leftOrNull(): A? = when (this) {
        is Left -> this.value
        is Right -> null
    }

    /**
     * 右側の値を取得します。
     * @return 値がない場合は`null`
     */
    fun getOrNull(): B? = when (this) {
        is Left -> null
        is Right -> this.value
    }

    /**
     * 左側の値がある場合に処理を行います。
     * @param action [Left]の場合に実行されるブロック
     */
    inline fun onLeft(action: (A) -> Unit): Either<A, B> {
        contract {
            callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        }
        if (this is Left<A>) {
            action(this.value)
        }
        return this
    }

    /**
     * 右側の値がある場合に処理を行います。
     * @param action [Right]の場合に実行されるブロック
     */
    inline fun onRight(action: (B) -> Unit): Either<A, B> {
        contract {
            callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        }
        if (this is Right<B>) {
            action(this.value)
        }
        return this
    }

    /**
     * 左右の値を入れ替えます。
     */
    fun swap(): Either<B, A> = when (this) {
        is Left -> Right(this.value)
        is Right -> Left(this.value)
    }

    /**
     * [Pair]に変換します。
     */
    fun toPair(): Pair<A?, B?> = this.fold({ it to null }, { null to it })

    /**
     * [Ior]に変換します。
     */
    fun toIor(): Ior<A, B> = this.fold({ Ior.Left(it) }, { Ior.Right(it) })

    /**
     * 保持している値を変換します。
     * @param C 変換後のクラス
     * @param right 右側の値を変換するブロック
     */
    inline fun <C> map(right: (B) -> C): Either<A, C> {
        contract {
            callsInPlace(right, InvocationKind.AT_MOST_ONCE)
        }
        return when (this) {
            is Left -> this
            is Right -> right(this.value).let(::Right)
        }
    }

    /**
     * 保持している値を変換します。
     * @param C 変換後のクラス
     * @param left 左側の値を変換するブロック
     */
    inline fun <C> mapLeft(left: (A) -> C): Either<C, B> {
        contract {
            callsInPlace(left, InvocationKind.AT_MOST_ONCE)
        }
        return when (this) {
            is Left -> left(this.value).let(::Left)
            is Right -> this
        }
    }

    /**
     * 保持している値を変換します。
     * @param C 変換後のクラス
     * @param left 左側の値を変換するブロック
     * @param right 右側の値を変換するブロック
     */
    inline fun <C> fold(left: (A) -> C, right: (B) -> C): C {
        contract {
            callsInPlace(left, InvocationKind.AT_MOST_ONCE)
            callsInPlace(right, InvocationKind.AT_MOST_ONCE)
        }
        return when (this) {
            is Left -> left(this.value)
            is Right -> right(this.value)
        }
    }

    /**
     * 左側の値だけを保持する[Either]の実装クラスです。
     */
    data class Left<out A>(val value: A) : Either<A, Nothing>()

    /**
     * 右側の値だけを保持する[Either]の実装クラスです。
     */
    data class Right<out B>(val value: B) : Either<Nothing, B>()
}

//    Extension    //

/**
 * [Either.Left]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <A> A.left(): Either<A, Nothing> = Either.Left(this)

/**
 * [Either.Right]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <B> B.right(): Either<Nothing, B> = Either.Right(this)

/**
 * 左右の値が同じクラスの場合，値を取り出します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T> Either<T, T>.unwrap(): T = this.fold(identity(), identity())

/**
 * 右側の値を取得します。
 * @param default 左側の値を右側に変換するブロック
 * @return 右側の値がない場合は`default`の戻り値
 */
inline fun <A, B> Either<A, B>.getOrElse(default: (A) -> B): B {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Either.Left -> default(this.value)
        is Either.Right -> this.value
    }
}

/**
 * 保持している値を別の[Either]に変換します。
 * @param C 変換後の値クラス
 * @param right 右側の値を[Either]に変換するブロック
 */
inline fun <A, B, C> Either<A, B>.flatMap(right: (B) -> Either<A, C>): Either<A, C> {
    contract {
        callsInPlace(right, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Either.Left -> this
        is Either.Right -> right(this.value)
    }
}

/**
 * 保持している値を別の[Either]に変換します。
 * @param C 変換後の値クラス
 * @param left 左側の値を[Either]に変換するブロック
 */
inline fun <A, B, C> Either<A, B>.flatMapLeft(left: (A) -> Either<C, B>): Either<C, B> {
    contract {
        callsInPlace(left, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Either.Left -> left(this.value)
        is Either.Right -> this
    }
}
