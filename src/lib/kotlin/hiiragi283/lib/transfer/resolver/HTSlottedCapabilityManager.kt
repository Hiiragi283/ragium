package hiiragi283.lib.transfer.resolver

import hiiragi283.lib.transfer.holder.HTCapabilityHolder
import net.minecraft.core.Direction

/**
 * [HTSlottedCapabilityResolver]の実装クラスです。
 *
 * 参照 : [Mekanism - CapabilityHandlerManager](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/common/capabilities/resolver/manager/CapabilityHandlerManager.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
open class HTSlottedCapabilityManager<HOLDER : HTCapabilityHolder, SLOT : Any, HANDLER : Any>(
    holder: HOLDER,
    proxyCreator: ProxyCreator<HOLDER, HANDLER>,
    private val containerGetter: (HOLDER, Direction?) -> List<SLOT>,
) : HTCapabilityManager<HOLDER, HANDLER>(holder, proxyCreator),
    HTSlottedCapabilityResolver<SLOT> {
    override fun isEmpty(side: Direction?): Boolean = getContainers(side).isEmpty()

    override fun getContainers(side: Direction?): List<SLOT> = containerGetter(holder, side)
}
