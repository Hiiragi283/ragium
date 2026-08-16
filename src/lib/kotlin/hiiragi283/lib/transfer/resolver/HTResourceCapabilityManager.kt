package hiiragi283.lib.transfer.resolver

import hiiragi283.lib.transfer.HTResourceHandler
import hiiragi283.lib.transfer.HTResourceSlot
import hiiragi283.lib.transfer.holder.HTResourceSlotHolder
import hiiragi283.lib.transfer.proxy.HTProxyResourceHandler
import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.resource.Resource

/**
 * [ResourceHandler]向けの[HTCapabilityManagerImpl]の実装クラスです。
 *
 * 参照 : [Mekanism - ResourceHandlerManager](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/capabilities/resolver/manager/ResourceHandlerManager.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 **/
class HTResourceCapabilityManager<RESOURCE : Resource, SLOT : HTResourceSlot<RESOURCE>>(holder: HTResourceSlotHolder<SLOT>) :
    HTCapabilityManagerImpl<HTResourceSlotHolder<SLOT>, SLOT, ResourceHandler<RESOURCE>>(
        holder,
        { side: Direction?, holderIn: HTResourceSlotHolder<SLOT> ->
            HTProxyResourceHandler(
                HTResourceHandler(holderIn.getSlots(side)),
                side,
                holderIn,
            )
        },
        HTResourceSlotHolder<SLOT>::getSlots,
    )
