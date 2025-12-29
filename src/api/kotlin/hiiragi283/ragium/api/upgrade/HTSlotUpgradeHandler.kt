package hiiragi283.ragium.api.upgrade

import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.HTItemSlot

interface HTSlotUpgradeHandler : HTUpgradeHandler {
    fun getUpgradeSlots(): List<HTItemSlot>

    override fun getUpgrades(): List<HTItemResourceType> = getUpgradeSlots().mapNotNull(HTItemSlot::getResource)
}
