package hiiragi283.ragium.common.storage.resolver

import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.HTSidedItemHandler
import hiiragi283.ragium.common.storage.proxy.HTProxyItemHandler
import net.neoforged.neoforge.items.IItemHandler

/**
 * @see [mekanism.common.capabilities.resolver.manager.ItemHandlerManager]
 */
class HTItemHandlerManager(holder: HTItemSlotHolder?, baseHandler: HTSidedItemHandler) :
    HTCapabilityManagerImpl<HTItemSlotHolder, HTItemSlot, IItemHandler, HTSidedItemHandler>(
        holder,
        baseHandler,
        ::HTProxyItemHandler,
        HTItemSlotHolder::getItemSlot,
    )
