package hiiragi283.lib.transfer.resolver

import hiiragi283.lib.transfer.holder.HTCapabilityHolder
import net.minecraft.core.Direction

/**
 * [HTCapabilityManager]の実装クラスです。
 *
 * 参照 : [Mekanism - CapabilityHandlerManager](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/capabilities/resolver/manager/CapabilityHandlerManager.java)
 *       [Mekanism - BasicCapabilityResolver](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/capabilities/resolver/BasicCapabilityResolver.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
open class HTCapabilityManagerImpl<HOLDER : HTCapabilityHolder, SLOT : Any, HANDLER : Any>(
    protected val holder: HOLDER,
    private val proxyCreator: ProxyCreator<HOLDER, HANDLER>,
    private val containerGetter: (HOLDER, Direction?) -> List<SLOT>,
) : HTCapabilityManager<SLOT> {
    private val handlers: MutableMap<Direction, HANDLER> = mutableMapOf()
    private var readOnlyHandler: HANDLER? = null

    override fun <T : Any> resolve(side: Direction?): T? = when {
        getContainers(side).isEmpty() -> null
        else -> resolveInternal(side)
    }

    override fun getContainers(side: Direction?): List<SLOT> = containerGetter(holder, side)

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> resolveInternal(side: Direction?): T? {
        if (side == null) {
            if (readOnlyHandler == null) {
                readOnlyHandler = proxyCreator.create(null, holder)
            }
            return readOnlyHandler as? T
        }
        var handler: HANDLER? = handlers[side]
        if (handler == null) {
            handler = proxyCreator.create(side, holder)
            handlers[side] = handler
        }
        return handler as? T
    }

    fun interface ProxyCreator<HOLDER, HANDLER> {
        fun create(side: Direction?, holder: HOLDER): HANDLER
    }
}
