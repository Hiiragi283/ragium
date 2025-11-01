package hiiragi283.ragium.api.registry

/**
 * @see mekanism.common.registration.DoubleWrappedRegistryObject
 */
open class HTDoubleDeferredHolder<R_FIRST : Any, FIRST : R_FIRST, R_SECOND : Any, SECOND : R_SECOND>(
    first: HTDeferredHolder<R_FIRST, FIRST>,
    protected val second: HTDeferredHolder<R_SECOND, SECOND>,
) : HTDeferredHolder<R_FIRST, FIRST>(first.key) {
    fun getSecond(): SECOND = second.get()
}
