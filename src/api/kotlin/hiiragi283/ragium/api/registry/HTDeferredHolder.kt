package hiiragi283.ragium.api.registry

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredHolder

/**
 * @see [mekanism.common.registration.MekanismDeferredHolder]
 */
open class HTDeferredHolder<R : Any, T : R>(key: ResourceKey<R>) :
    DeferredHolder<R, T>(key),
    HTHolderLike {
    companion object {
        @JvmStatic
        fun <R : Any, T : R> create(key: ResourceKey<out Registry<R>>, id: ResourceLocation): HTDeferredHolder<R, T> =
            create(ResourceKey.create(key, id))

        @JvmStatic
        fun <R : Any, T : R> create(key: ResourceKey<R>): HTDeferredHolder<R, T> = HTDeferredHolder(key)

        @JvmStatic
        fun <R : Any> createSimple(key: ResourceKey<out Registry<R>>, id: ResourceLocation): HTDeferredHolder<R, *> =
            createSimple(ResourceKey.create(key, id))

        @JvmStatic
        fun <R : Any> createSimple(key: ResourceKey<R>): HTDeferredHolder<R, *> = HTDeferredHolder(key)
    }

    fun isOf(value: R): Boolean = this.value() == value
}
