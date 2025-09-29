package hiiragi283.ragium.common.storage.resolver

import hiiragi283.ragium.api.storage.holder.HTCapabilityHolder
import hiiragi283.ragium.api.storage.resolver.HTCapabilityManager
import net.minecraft.core.Direction
import net.neoforged.neoforge.capabilities.BlockCapability

/**
 * @see [mekanism.common.capabilities.resolver.manager.CapabilityHandlerManager]
 */
open class HTCapabilityManagerImpl<HOLDER : HTCapabilityHolder, CONTAINER : Any, HANDLER : Any, SIDED_HANDLER : HANDLER>(
    @JvmField protected val holder: HOLDER?,
    baseHandler: SIDED_HANDLER,
    proxyCreator: ProxyCreator<HANDLER, SIDED_HANDLER>,
    private val containerGetter: (HOLDER, Direction?) -> List<CONTAINER>,
) : HTCapabilityResolverImpl<HANDLER, SIDED_HANDLER>(
        baseHandler,
        proxyCreator,
    ),
    HTCapabilityManager<CONTAINER> {
    override fun canHandle(): Boolean = holder != null

    override fun getContainers(side: Direction?): List<CONTAINER> = when (canHandle()) {
        true -> containerGetter(holder!!, side)
        false -> listOf()
    }

    override fun getHolder(): HOLDER? = holder

    override fun <T : Any> resolve(capability: BlockCapability<T, Direction?>, context: Direction?): T? = when {
        getContainers(context).isEmpty() -> null
        else -> super.resolve(capability, context)
    }
}
