package hiiragi283.lib.resource

import hiiragi283.lib.util.Ior
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

/**
 * シンプルな[HTKeyOrValue]のエイリアスです。
 * @param R レジストリの値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
typealias HTSimpleKeyOrValue<R> = HTKeyOrValue<R, R>

/**
 * [ID][ResourceKey]または値を提供する[HTIdOrValue]の拡張インターフェースです。
 * @param R レジストリの値のクラス
 * @param T 提供する値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
fun interface HTKeyOrValue<R : Any, out T : R> : HTIdOrValue<T> {
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

    override fun unwrapWithId(): Ior<Identifier, T> = unwrapWithKey().mapLeft(ResourceKey<R>::identifier)
}
