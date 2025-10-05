package hiiragi283.ragium.common.storage.resolver

import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.holder.HTCapabilityHolder
import net.minecraft.core.Direction
import net.neoforged.neoforge.capabilities.BlockCapability

/**
 * @see [mekanism.common.capabilities.resolver.BasicSidedCapabilityResolver]
 * @see [mekanism.common.capabilities.resolver.manager.CapabilityHandlerManager]
 */
open class HTCapabilityManagerImpl<HOLDER : HTCapabilityHolder, CONTAINER : Any, HANDLER : Any, SIDED_HANDLER : HANDLER>(
    protected val holder: HOLDER?,
    private val baseHandler: SIDED_HANDLER,
    private val proxyCreator: ProxyCreator<HANDLER, SIDED_HANDLER>,
    private val containerGetter: (HOLDER, Direction?) -> List<CONTAINER>,
) : HTCapabilityManager<CONTAINER> {
    private val handlers: MutableMap<Direction, HANDLER> = mutableMapOf()
    private var readOnlyHandler: HANDLER? = null

    fun <T : Any, U : T> resolve(type: HTMultiCapability<T, U>, context: Direction?): T? = resolve(type.blockCapability, context)

    override fun <T : Any> resolve(capability: BlockCapability<T, Direction?>, side: Direction?): T? = when {
        getContainers(side).isEmpty() -> null
        else -> resolveInternal(capability, side)
    }

    override fun canHandle(): Boolean = holder != null

    override fun getContainers(side: Direction?): List<CONTAINER> = when (canHandle()) {
        true -> containerGetter(holder!!, side)
        false -> listOf()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> resolveInternal(capability: BlockCapability<T, Direction?>, side: Direction?): T? {
        if (side == null) {
            if (readOnlyHandler == null) {
                readOnlyHandler = proxyCreator.create(baseHandler, null, holder)
            }
            return readOnlyHandler as? T
        }
        var handler: HANDLER? = handlers[side]
        if (handler == null) {
            handler = proxyCreator.create(baseHandler, side, holder)
            handlers[side] = handler
        }
        return handler as? T
    }

    fun interface ProxyCreator<HANDLER : Any, SIDED_HANDLER : HANDLER> {
        fun create(handler: SIDED_HANDLER, side: Direction?, holder: HTCapabilityHolder?): HANDLER
    }
}
