package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.extension.andThen
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus

/**
 * @see [mekanism.common.registration.DoubleDeferredRegister]
 */
open class HTDoubleDeferredRegister<FIRST : Any, SECOND : Any> protected constructor(
    protected val firstRegister: HTDeferredRegister<FIRST>,
    protected val secondRegister: HTDeferredRegister<SECOND>,
) {
    fun <F : FIRST, S : SECOND, H : HTDoubleDeferredHolder<FIRST, F, SECOND, S>> registerEach(
        name: String,
        first: (ResourceLocation) -> F,
        second: (ResourceLocation) -> S,
        wrapper: (HTDeferredHolder<FIRST, F>, HTDeferredHolder<SECOND, S>) -> H,
    ): H = wrapper(firstRegister.register(name, first), secondRegister.register(name, second))

    fun <F : FIRST, S : SECOND, H : HTDoubleDeferredHolder<FIRST, F, SECOND, S>> register(
        name: String,
        first: (ResourceLocation) -> F,
        second: (F) -> S,
        wrapper: (HTDeferredHolder<FIRST, F>, HTDeferredHolder<SECOND, S>) -> H,
    ): H = registerAdvanced(name, first, HTDeferredHolder<FIRST, F>::get.andThen(second), wrapper)

    fun <F : FIRST, S : SECOND, H : HTDoubleDeferredHolder<FIRST, F, SECOND, S>> registerAdvanced(
        name: String,
        first: (ResourceLocation) -> F,
        second: (HTDeferredHolder<FIRST, F>) -> S,
        wrapper: (HTDeferredHolder<FIRST, F>, HTDeferredHolder<SECOND, S>) -> H,
    ): H {
        val firstHolder: HTDeferredHolder<FIRST, F> = firstRegister.register(name, first)
        return wrapper(firstHolder, secondRegister.register(name) { _: ResourceLocation -> second(firstHolder) })
    }

    fun register(bus: IEventBus) {
        firstRegister.register(bus)
        secondRegister.register(bus)
    }

    val firstEntries: Collection<HTDeferredHolder<FIRST, out FIRST>> get() = firstRegister.entries
    val secondEntries: Collection<HTDeferredHolder<SECOND, out SECOND>> get() = secondRegister.entries

    fun addFirstAlias(from: String, to: String) {
        firstRegister.addAlias(firstRegister.createId(from), firstRegister.createId(to))
    }

    fun addSecondAlias(from: String, to: String) {
        secondRegister.addAlias(secondRegister.createId(from), secondRegister.createId(to))
    }
}
