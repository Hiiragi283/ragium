package hiiragi283.lib.registry

import hiiragi283.lib.resource.HTKeyOrValue
import hiiragi283.lib.util.Ior
import hiiragi283.lib.util.fold
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.DeferredHolder

/**
 * シンプルな[HTDeferredHolder]のエイリアスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
typealias HTSimpleDeferredHolder<R> = HTDeferredHolder<R, R>

/**
 * Hiiragi Seriesで使用される[DeferredHolder]の拡張クラスです。
 * @param R レジストリの要素のクラス
 * @param T 要素のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
open class HTDeferredHolder<R : Any, out T : R> :
    DeferredHolder<R, @UnsafeVariance T>,
    HTKeyOrValue<R, T> {
    constructor(key: ResourceKey<R>) : super(key)

    constructor(key: RegistryKey<R>, id: Identifier) : super(key.createKey(id))

    final override fun unwrapWithKey(): Ior<ResourceKey<R>, T> = asOptional().fold({ Ior.Left(this.key) }, { Ior.Both(this.key, it) })
}
