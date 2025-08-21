package hiiragi283.ragium.api.registry

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

/**
 * @see [mekanism.common.registration.DoubleDeferredRegister]
 */
open class HTDoubleDeferredRegister<FIRST : Any, SECOND : Any> protected constructor(
    protected val firstRegister: DeferredRegister<FIRST>,
    protected val secondRegister: DeferredRegister<SECOND>,
) {
    protected constructor(
        namespace: String,
        first: ResourceKey<out Registry<FIRST>>,
        second: DeferredRegister<SECOND>,
    ) : this(DeferredRegister.create(first, namespace), second)

    protected constructor(
        namespace: String,
        first: ResourceKey<out Registry<FIRST>>,
        second: ResourceKey<out Registry<SECOND>>,
    ) : this(namespace, first, DeferredRegister.create(second, namespace))

    fun <F : FIRST, S : SECOND, H : HTDoubleDeferredHolder<FIRST, F, SECOND, S>> registerEach(
        name: String,
        first: (ResourceLocation) -> F,
        second: (ResourceLocation) -> S,
        wrapper: (DeferredHolder<FIRST, F>, DeferredHolder<SECOND, S>) -> H,
    ): H = wrapper(firstRegister.register(name, first), secondRegister.register(name, second))

    fun <F : FIRST, S : SECOND, H : HTDoubleDeferredHolder<FIRST, F, SECOND, S>> register(
        name: String,
        first: (ResourceLocation) -> F,
        second: (F) -> S,
        wrapper: (DeferredHolder<FIRST, F>, DeferredHolder<SECOND, S>) -> H,
    ): H = registerAdvanced(name, first, { holder: DeferredHolder<FIRST, F> -> second(holder.get()) }, wrapper)

    fun <F : FIRST, S : SECOND, H : HTDoubleDeferredHolder<FIRST, F, SECOND, S>> registerAdvanced(
        name: String,
        first: (ResourceLocation) -> F,
        second: (DeferredHolder<FIRST, F>) -> S,
        wrapper: (DeferredHolder<FIRST, F>, DeferredHolder<SECOND, S>) -> H,
    ): H {
        val firstHolder: DeferredHolder<FIRST, F> = firstRegister.register(name, first)
        return wrapper(firstHolder, secondRegister.register(name) { _: ResourceLocation -> second(firstHolder) })
    }

    fun register(bus: IEventBus) {
        firstRegister.register(bus)
        secondRegister.register(bus)
    }

    val firstEntries: Collection<DeferredHolder<FIRST, out FIRST>> get() = firstRegister.entries
    val secondEntries: Collection<DeferredHolder<SECOND, out SECOND>> get() = secondRegister.entries
}
