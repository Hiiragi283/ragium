package hiiragi283.ragium.api.registry

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Function
import java.util.function.Supplier

/**
 * @see [mekanism.common.registration.MekanismDeferredRegister]
 */
open class HTDeferredRegister<T : Any>(registryKey: ResourceKey<out Registry<T>>, namespace: String) :
    DeferredRegister<T>(registryKey, namespace) {
    override fun getEntries(): Collection<HTDeferredHolder<T, out T>> = super.getEntries().filterIsInstance<HTDeferredHolder<T, out T>>()

    override fun <I : T> register(name: String, func: Function<ResourceLocation, out I>): HTDeferredHolder<T, I> =
        super.register(name, func) as HTDeferredHolder<T, I>

    override fun <I : T> register(name: String, sup: Supplier<out I>): HTDeferredHolder<T, I> =
        super.register(name, sup) as HTDeferredHolder<T, I>

    override fun <I : T> createHolder(registryKey: ResourceKey<out Registry<T>>, key: ResourceLocation): HTDeferredHolder<T, I> =
        HTDeferredHolder.create(registryKey, key)
}
