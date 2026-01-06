package hiiragi283.ragium.common.upgrade

import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.ragium.api.data.map.HTUpgradeData
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.upgrade.HTUpgradeHandler
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.world.item.ItemStack

class HTComponentUpgradeHandler(private val attachedTo: ItemStack) : HTUpgradeHandler {
    private fun getComponent(): Collection<ItemStack> = attachedTo.get(RagiumDataComponents.MACHINE_UPGRADES) ?: listOf()

    override fun getUpgrades(): List<HTItemResourceType> = getComponent().mapNotNull(ItemStack::toResource)

    override fun isValidUpgrade(upgrade: HTItemResourceType, existing: List<HTItemResourceType>): Boolean {
        val upgradeData: HTUpgradeData = RagiumDataMapTypes.getUpgradeData(upgrade) ?: return false
        val isTarget: Boolean = upgradeData.isTarget(attachedTo)
        val isCompatible: Boolean = existing.all { resource: HTItemResourceType -> HTUpgradeData.areCompatible(upgrade, resource) }
        return isTarget && isCompatible
    }
}
