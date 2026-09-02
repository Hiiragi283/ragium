package hiiragi283.lib.util

import java.util.Optional
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.toList

/**
 * [Optional]に変換します。
 * @return [this]が`null`の場合は[Optional.empty]，それ以外の場合は[Optional.of]
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun <T : Any> T?.toOptional(): Optional<T> = Optional.ofNullable(this)

/**
 * [Optional.of]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun <T : Any> T.some(): Optional<T> = Optional.of(this)

/**
 * [Pair]の[Optional]を[Map]に変換します。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun <K, V> Optional<Pair<K, V>>.toMap(): Map<K, V> = this.toList().toMap()

/**
 * 保持している値を変換します。
 * @param R 変換後のクラス
 * @param empty 値がない場合に実行されるブロック
 * @param some 値を変換するブロック
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun <T : Any, R : Any> Optional<T>.fold(empty: () -> R, some: (T) -> R): R = this.map(some).getOrElse(empty)

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun <T : Any, R : Any> Optional<T>.flatMapNullable(transform: (T) -> R?): Optional<R> = this.flatMap { transform(it).toOptional() }

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun <R : Any, L> Optional<R>.toEither(empty: () -> L): Either<L, R> = this.fold({ empty().left() }, { it.right() })

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
fun <R : Any, L> Optional<R>.toIor(empty: () -> L): Ior<L, R> = this.fold({ Ior.Left(empty()) }, { Ior.Both(empty(), it) })
