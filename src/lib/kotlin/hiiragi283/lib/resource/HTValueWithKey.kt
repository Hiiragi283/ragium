package hiiragi283.lib.resource

import hiiragi283.lib.util.Ior
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

/**
 * シンプルな[HTValueWithKey]のエイリアスです。
 * @param R レジストリの値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
typealias HTSimpleValueWithKey<R> = HTValueWithKey<R, R>

/**
 * [ID][ResourceKey]または値を提供するインターフェースです。
 * @param R レジストリの値のクラス
 * @param T 提供する値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun interface HTValueWithKey<R : Any, out T : R> {
    /**
     * 保持している値を[Ior]に変換します。
     */
    fun unwrapWithKey(): Ior<ResourceKey<R>, T>

    /**
     * [ID][ResourceKey]を取得します。
     */
    val keyOrNull: ResourceKey<R>? get() = unwrapWithKey().getLeft()

    /**
     * [ID][ResourceKey]を取得します。
     * @throws IllegalStateException [keyOrNull]が`null`の場合
     */
    val keyOrThrow: ResourceKey<R> get() = keyOrNull ?: error("Unregistered value for ${getOrThrow()}")

    /**
     * [ID][Identifier]を取得します。
     */
    val idOrNull: Identifier? get() = keyOrNull?.identifier()

    /**
     * [ID][Identifier]を取得します。
     * @throws IllegalStateException [idOrNull]が`null`の場合
     */
    val idOrThrow: Identifier get() = idOrNull ?: error("Unknown id for ${getOrThrow()}")

    /**
     * 値を取得します。
     */
    fun getOrNull(): T? = unwrapWithKey().getRight()

    /**
     * 値を取得します。
     * @throws IllegalStateException [getOrNull]が`null`の場合
     */
    fun getOrThrow(): T = getOrNull() ?: error("Unknown value for $idOrThrow")
}
