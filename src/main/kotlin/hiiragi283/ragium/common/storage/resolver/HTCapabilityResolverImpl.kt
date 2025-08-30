package hiiragi283.ragium.common.storage.resolver

import hiiragi283.ragium.api.storage.holder.HTCapabilityHolder
import hiiragi283.ragium.api.storage.resolver.HTCapabilityResolver
import net.minecraft.core.Direction
import net.neoforged.neoforge.capabilities.BlockCapability

/**
 * @see [mekanism.common.capabilities.resolver.BasicSidedCapabilityResolver]
 */
open class HTCapabilityResolverImpl<HANDLER : Any, SIDED_HANDLER : HANDLER>(
    private val baseHandler: SIDED_HANDLER,
    private val proxyCreator: ProxyCreator<HANDLER, SIDED_HANDLER>,
) : HTCapabilityResolver<Direction> {
    private val handlers: MutableMap<Direction, HANDLER> = mutableMapOf()
    private var readOnlyHandler: HANDLER? = null

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> resolve(capability: BlockCapability<T, Direction?>, context: Direction?): T? {
        if (context == null) {
            if (readOnlyHandler == null) {
                readOnlyHandler = proxyCreator.create(baseHandler, null, getHolder())
            }
            return readOnlyHandler as? T
        }
        var handler: HANDLER? = handlers[context]
        if (handler == null) {
            handler = proxyCreator.create(baseHandler, context, getHolder())
            handlers[context] = handler
        }
        return handler as? T
    }

    override fun invalidate(capability: BlockCapability<*, Direction>, context: Direction?) {
        when (context) {
            null -> readOnlyHandler = null
            else -> handlers.remove(context)
        }
    }

    override fun invalidateAll() {
        readOnlyHandler = null
        handlers.clear()
    }

    protected open fun getHolder(): HTCapabilityHolder? = null

    fun interface ProxyCreator<HANDLER : Any, SIDED_HANDLER : HANDLER> {
        fun create(handler: SIDED_HANDLER, side: Direction?, holder: HTCapabilityHolder?): HANDLER
    }
}
