package hiiragi283.ragium.api.registry

import net.neoforged.neoforge.registries.DeferredHolder

/**
 * @see [mekanism.common.registration.DoubleWrappedRegistryObject]
 */
open class HTDoubleDeferredHolder<R_FIRST : Any, FIRST : R_FIRST, R_SECOND : Any, SECOND : R_SECOND>(
    first: DeferredHolder<R_FIRST, FIRST>,
    protected val second: DeferredHolder<R_SECOND, SECOND>,
) : DeferredHolder<R_FIRST, FIRST>(first.key) {
    fun getSecond(): SECOND = second.get()
}
