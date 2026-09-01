package hiiragi283.lib.resource

import hiiragi283.lib.util.Ior
import net.minecraft.resources.Identifier

/**
 * [ID][Identifier]または値を提供するインターフェースです。
 * @param T 提供する値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun interface HTIdOrValue<out T : Any> {
    /**
     * 保持している値を[Ior]に変換します。
     */
    fun unwrapWithId(): Ior<Identifier, T>

    /**
     * [ID][Identifier]を取得します。
     */
    val idOrNull: Identifier? get() = unwrapWithId().getLeft()

    /**
     * [ID][Identifier]を取得します。
     * @throws IllegalStateException [idOrNull]が`null`の場合
     */
    val idOrThrow: Identifier get() = idOrNull ?: error("Unknown id for ${getOrThrow()}")

    /**
     * 値を取得します。
     */
    fun getOrNull(): T? = unwrapWithId().getRight()

    /**
     * 値を取得します。
     * @throws IllegalStateException [getOrNull]が`null`の場合
     */
    fun getOrThrow(): T = getOrNull() ?: error("Unknown value for $idOrThrow")
}
