package hiiragi283.ragium.api.registry

import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredHolder

/**
 * @see [mekanism.common.registration.MekanismDeferredHolder]
 */
open class HTDeferredHolder<R : Any, T : R> :
    DeferredHolder<R, T>,
    HTHolderLike {
    constructor(key: ResourceKey<R>) : super(key)

    constructor(key: RegistryKey<R>, id: ResourceLocation) : super(key.createKey(id))

    companion object {
        @JvmStatic
        fun <R : Any> createSimple(key: RegistryKey<R>, id: ResourceLocation): HTDeferredHolder<R, *> = createSimple(key.createKey(id))

        @JvmStatic
        fun <R : Any> createSimple(key: ResourceKey<R>): HTDeferredHolder<R, *> = HTDeferredHolder(key)
    }

    override fun getKey(): ResourceKey<R> = super.key

    fun isOf(value: R): Boolean = this.value() == value
}
