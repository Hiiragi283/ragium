package hiiragi283.lib.resource

import hiiragi283.lib.util.Ior
import net.minecraft.resources.ResourceKey

/**
 * 常にキーと値を持つ[HTValueWithKey]の実装クラスです。
 * @param R レジストリの値のクラス
 * @param T 提供する値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
@JvmRecord
data class HTValueAndKey<R : Any, out T : R>(val key: ResourceKey<R>, val value: T) : HTValueWithKey<R, T> {
    override fun unwrapWithKey(): Ior<ResourceKey<R>, T> = Ior.Both(key, value)

    override val keyOrNull: ResourceKey<R> get() = key

    override fun getOrNull(): T = value
}
