package hiiragi283.lib.resource

import net.minecraft.resources.ResourceKey

typealias SimpleSupplierWithKey<R> = SupplierWithKey<R, R>

/**
 * [キー][ResourceKey]を提供する[SupplierWithId]の拡張インターフェースです。
 * @param T 保持している値のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
interface SupplierWithKey<R : Any, out T : R> :
    SupplierWithId<T>,
    HTKeyLike<R>
