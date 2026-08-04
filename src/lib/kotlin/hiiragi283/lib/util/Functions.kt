package hiiragi283.lib.util

/**
 * 同じクラスの値を返す処理を表すエイリアスです。
 *
 * 参照 : [Java - UnaryOperator][java.util.function.UnaryOperator]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias Identity<T> = (T) -> T

/**
 * 恒等操作を行うブロックを返します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun <T> identity(): Identity<T> = { it }
