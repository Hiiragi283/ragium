package hiiragi283.lib.registry

import hiiragi283.lib.resource.SupplierWithKey
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.Option
import hiiragi283.lib.util.kotlin
import hiiragi283.lib.util.toTextResult
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.DeferredHolder

/**
 * Hiiragi Seriesで使用される[DeferredHolder]の拡張クラスです。
 * @param R レジストリの要素のクラス
 * @param T 要素のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
open class HTDeferredHolder<R : Any, out T : R> :
    DeferredHolder<R, @UnsafeVariance T>,
    SupplierWithKey<R, T> {
    constructor(key: ResourceKey<R>) : super(key)

    constructor(key: RegistryKey<R>, id: Identifier) : super(key.createKey(id))

    /**
     * @since 26.1.4
     */
    fun getOrNull(): T? = if (this.isBound) get() else null

    /**
     * @since 26.1.4
     */
    fun getResult(): HTTextResult<T> = getOrNull().toTextResult { "Trying to access unbound value: $key" }

    /**
     * @since 26.1.4
     */
    fun asOption(): Option<T> = asOptional().kotlin

    override fun getId(): Identifier = super<DeferredHolder>.getId()
}
