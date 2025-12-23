package hiiragi283.ragium.api.upgrade

import hiiragi283.core.api.stack.ImmutableItemStack
import hiiragi283.core.api.storage.item.HTItemSlot

interface HTSlotUpgradeHandler : HTUpgradeHandler {
    fun getUpgradeSlots(): List<HTItemSlot>

    override fun getUpgrades(): List<ImmutableItemStack> = getUpgradeSlots().mapNotNull(HTItemSlot::getStack)
}
