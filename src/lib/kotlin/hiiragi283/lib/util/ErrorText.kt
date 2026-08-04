@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.util

import hiiragi283.lib.text.HTHasText
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.toText
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import org.slf4j.Logger

/**
 * 結果を[ErrorText]または値で表現する[Either]のエイリアスです。
 * @param T 値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias HTTextResult<T> = Either<ErrorText, T>

/**
 * 新しい[HTTextResult]のインスタンスを作成します。
 * @param value エラーメッセージ
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun HTTextResult(value: String): HTTextResult<Nothing> = ErrorText(value).left()

/**
 * [HTTextResult]に変換します。
 * @param T 値のクラス
 * @param message エラーメッセージを提供するブロック
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun <T> T?.toTextResult(message: () -> String): HTTextResult<T> {
    contract {
        callsInPlace(message, InvocationKind.AT_MOST_ONCE)
    }
    return this?.right() ?: HTTextResult(message())
}

/**
 * エラーメッセージがある場合，それをログに出力します。
 * @param logger ログの出力先
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T> HTTextResult<T>.printError(logger: Logger): HTTextResult<T> = this.onLeft { logger.error(it.value) }

/**
 * 保持している値を取得します。
 * @throws IllegalStateException 値がない場合
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T> HTTextResult<T>.getOrThrow(): T = this.getOrElse { error(it.value) }

/**
 * エラーメッセージのラッパークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmInline
value class ErrorText(val value: String) : HTHasText {
    override fun getText(): Text = value.toText()
}
