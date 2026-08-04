@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.util

import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 選択的に値を保持するクラスです。
 *
 * 参照 : [Arrow - Option](https://github.com/arrow-kt/arrow/blob/main/arrow-libs/core/arrow-core/src/commonMain/kotlin/arrow/core/Option.kt)
 * @param T 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmInline
value class Option<out T : Any>@PublishedApi internal constructor(@PublishedApi internal val value: Any?) {
    companion object {
        @JvmStatic
        private val EMPTY: Option<Nothing> = Option(null)

        /**
         * 新しい[Option]のインスタンスを作成します。
         * @return [value]が`null`の場合は[none]，それ以外の場合は[some]
         */
        @JvmStatic
        fun <T : Any> fromNullable(value: T?): Option<T> = if (value == null) none() else some(value)

        /**
         * 新しい[Option]のインスタンスを作成します。
         * @param value 保持している値
         */
        @JvmStatic
        fun <T : Any> some(value: T): Option<T> = Option(value)

        /**
         * 値を保持しない[Option]のインスタンスを返します。
         */
        @JvmStatic
        fun <T : Any> none(): Option<T> = EMPTY
    }

    /**
     * 値があるかどうか判定します。
     * @return 値がある場合は`true`
     */
    fun isSome(): Boolean = value != null

    /**
     * 値がないかどうか判定します。
     * @return 値がない場合は`true`
     */
    fun isNone(): Boolean = value == null

    /**
     * 値を取得します。
     * @return 値がない場合は`null`
     */
    @Suppress("UNCHECKED_CAST")
    fun getOrNull(): T? = value as? T

    /**
     * 値がある場合に処理を行います。
     * @param action [isSome]の場合に実行されるブロック
     */
    inline fun onSome(action: (T) -> Unit): Option<T> {
        contract {
            callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        }
        getOrNull()?.let(action)
        return this
    }

    /**
     * 値がない場合に処理を行います。
     * @param action [isNone]の場合に実行されるブロック
     */
    inline fun onNone(action: () -> Unit): Option<T> {
        contract {
            callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        }
        if (this.isNone()) action()
        return this
    }

    /**
     * 保持している値を変換します。
     * @param R 変換後のクラス
     * @param transform 値を変換するブロック
     */
    inline fun <R : Any> map(transform: (T) -> R): Option<R> {
        contract {
            callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
        }
        return flatMap { some(transform(it)) }
    }

    /**
     * 保持している値を変換します。
     * @param R 変換後のクラス
     * @param empty 値がない場合に実行されるブロック
     * @param some 値を変換するブロック
     */
    inline fun <R> fold(empty: () -> R, some: (T) -> R): R {
        contract {
            callsInPlace(empty, InvocationKind.AT_MOST_ONCE)
            callsInPlace(some, InvocationKind.AT_MOST_ONCE)
        }
        return getOrNull()?.let(some) ?: empty()
    }

    /**
     * 保持している値を別の[Option]に変換します。
     * @param R 変換後の値クラス
     * @param transform 値を[Option]に変換するブロック
     */
    inline fun <R : Any> flatMap(transform: (T) -> Option<R>): Option<R> {
        contract {
            callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
        }
        return getOrNull()?.let(transform) ?: none()
    }

    /**
     * 保持している値を制限します。
     * @param predicate 値を制限するブロック
     * @return [predicate]の戻り値が`false`の場合は[none]
     */
    inline fun filter(predicate: (T) -> Boolean): Option<T> {
        contract {
            callsInPlace(predicate, InvocationKind.AT_MOST_ONCE)
        }
        return flatMap { if (predicate(it)) some(it) else none() }
    }

    /**
     * 保持している値を制限します。
     * @param predicate 値を制限するブロック
     * @return [predicate]の戻り値が`true`の場合は[none]
     */
    inline fun filterNot(predicate: (T) -> Boolean): Option<T> {
        contract {
            callsInPlace(predicate, InvocationKind.AT_MOST_ONCE)
        }
        return flatMap { if (!predicate(it)) some(it) else none() }
    }

    /**
     * [Either]に変換します。
     * @param L 左側の値のクラス
     * @param empty 左側の値を提供するブロック
     * @return 保持している値を右側とする[Either]
     */
    inline fun <L> toEither(empty: () -> L): Either<L, T> {
        contract {
            callsInPlace(empty, InvocationKind.AT_MOST_ONCE)
        }
        return fold({ empty().left() }, { it.right() })
    }

    /**
     * [Ior]に変換します。
     * @param L 左側の値のクラス
     * @param empty 左側の値を提供するブロック
     * @return 保持している値を右側とする[Ior]
     * @since 26.1.4
     */
    inline fun <L> toIor(empty: () -> L): Ior<L, T> = fold({ Ior.Left(empty()) }, { Ior.Both(empty(), it) })

    /**
     * [List]に変換します。
     */
    fun toList(): List<T> = fold(::emptyList, ::listOf)
}

//    Extension    //

/**
 * 値を取得します。
 * @return 値がない場合は`default`の戻り値
 */
inline fun <T : Any> Option<T>.getOrElse(default: () -> T): T {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return getOrNull() ?: default()
}

/**
 * [Option]に変換します。
 * @return [this]が`null`の場合は[Option.none]，それ以外の場合は[Option.some]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T : Any> T?.toOption(): Option<T> = Option.fromNullable(this)

/**
 * [Option.some]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T : Any> T.some(): Option<T> = Option.some(this)

/**
 * [Pair]の[Option]を[Map]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <K, V> Option<Pair<K, V>>.toMap(): Map<K, V> = this.toList().toMap()

//    Optional <-> Option    //

/**
 * [Optional]を[Option]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val <T : Any> Optional<T>.kotlin: Option<T> get() = this.map { it.some() }.orElseGet { Option.none() }

/**
 * [Option]を[Optional]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
val <T : Any> Option<T>.java: Optional<T> get() = this.fold({ Optional.empty() }, { Optional.of(it) })
