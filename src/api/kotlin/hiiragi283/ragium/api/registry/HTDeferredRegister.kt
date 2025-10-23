package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.function.IdToFunction
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

/**
 * @see [mekanism.common.registration.MekanismDeferredRegister]
 */
open class HTDeferredRegister<T : Any>(registryKey: RegistryKey<T>, namespace: String) : DeferredRegister<T>(registryKey, namespace) {
    fun createId(path: String): ResourceLocation = namespace.toId(path)

    open fun asSequence(): Sequence<HTDeferredHolder<T, out T>> = entries.asSequence()

    override fun getEntries(): Collection<HTDeferredHolder<T, out T>> = super.getEntries().filterIsInstance<HTDeferredHolder<T, out T>>()

    override fun <I : T> register(name: String, func: IdToFunction<out I>): HTDeferredHolder<T, I> =
        super.register(name, func) as HTDeferredHolder<T, I>

    override fun <I : T> register(name: String, sup: Supplier<out I>): HTDeferredHolder<T, I> =
        super.register(name, sup) as HTDeferredHolder<T, I>

    override fun <I : T> createHolder(registryKey: RegistryKey<T>, key: ResourceLocation): HTDeferredHolder<T, I> =
        HTDeferredHolder(registryKey, key)
}
