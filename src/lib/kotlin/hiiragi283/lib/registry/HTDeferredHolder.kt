package hiiragi283.lib.registry

import hiiragi283.lib.resource.HTValueWithKey
import hiiragi283.lib.resource.toLanguageKey
import hiiragi283.lib.text.HTHasText
import net.minecraft.core.TypedInstance
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
    HTValueWithKey<R, T> {
    constructor(key: ResourceKey<R>) : super(key)

    constructor(key: RegistryKey<R>, id: Identifier) : super(key.createKey(id))

    /**
     * @since 26.1.7
     */
    fun isOf(instance: TypedInstance<R>): Boolean = this.isBound && instance.`is`(this)

    final override fun key(): ResourceKey<R> = this.key

    final override fun asResult(): Result<T> = runCatching { get() }

    final override fun getOrNull(): T? = when (this.isBound) {
        true -> get()
        false -> null
    }

    final override fun getOrThrow(): T = get()

    /**
     * @param R レジストリの要素のクラス
     * @param T 要素のクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.8
     */
    open class Translatable<R : Any, out T : R> :
        HTDeferredHolder<R, T>,
        HTHasText.Translatable {
        constructor(key: ResourceKey<R>) : super(key)

        constructor(key: RegistryKey<R>, id: Identifier) : super(key, id)

        override val translationKey: String = key.toLanguageKey()
    }
}
