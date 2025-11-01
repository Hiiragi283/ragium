package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.function.IdToFunction
import hiiragi283.ragium.api.function.andThen
import net.neoforged.bus.api.IEventBus
import java.util.function.Supplier

/**
 * @see mekanism.common.registration.DoubleDeferredRegister
 */
open class HTDoubleDeferredRegister<FIRST : Any, SECOND : Any> protected constructor(
    protected val firstRegister: HTDeferredRegister<FIRST>,
    protected val secondRegister: HTDeferredRegister<SECOND>,
) {
    fun <F : FIRST, S : SECOND, H : HTDoubleDeferredHolder<FIRST, F, SECOND, S>> registerEach(
        name: String,
        first: Supplier<out F>,
        second: Supplier<out S>,
        combiner: HolderCombiner<FIRST, SECOND, F, S, H>,
    ): H = combiner.combine(firstRegister.register(name, first), secondRegister.register(name, second))

    fun <F : FIRST, S : SECOND, H : HTDoubleDeferredHolder<FIRST, F, SECOND, S>> registerEach(
        name: String,
        first: IdToFunction<F>,
        second: IdToFunction<S>,
        combiner: HolderCombiner<FIRST, SECOND, F, S, H>,
    ): H = combiner.combine(firstRegister.register(name, first), secondRegister.register(name, second))

    fun <F : FIRST, S : SECOND, H : HTDoubleDeferredHolder<FIRST, F, SECOND, S>> register(
        name: String,
        first: IdToFunction<F>,
        second: (F) -> S,
        combiner: HolderCombiner<FIRST, SECOND, F, S, H>,
    ): H = registerAdvanced(name, first, HTDeferredHolder<FIRST, F>::get.andThen(second), combiner)

    fun <F : FIRST, S : SECOND, H : HTDoubleDeferredHolder<FIRST, F, SECOND, S>> registerAdvanced(
        name: String,
        first: IdToFunction<F>,
        second: (HTDeferredHolder<FIRST, F>) -> S,
        combiner: HolderCombiner<FIRST, SECOND, F, S, H>,
    ): H {
        val firstHolder: HTDeferredHolder<FIRST, F> = firstRegister.register(name, first)
        return combiner.combine(firstHolder, secondRegister.register(name) { _ -> second(firstHolder) })
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

    fun interface HolderCombiner<FIRST : Any, SECOND : Any, F : FIRST, S : SECOND, H : HTDoubleDeferredHolder<FIRST, F, SECOND, S>> {
        fun combine(first: HTDeferredHolder<FIRST, F>, second: HTDeferredHolder<SECOND, S>): H
    }
}
