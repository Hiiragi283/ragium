package hiiragi283.ragium.common.upgrade

import hiiragi283.ragium.api.data.map.HTUpgradeData
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.upgrade.HTUpgradeHandler
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.world.item.ItemStack

class HTComponentUpgradeHandler(private val attachedTo: ItemStack) : HTUpgradeHandler {
    private fun getComponent(): Collection<ImmutableItemStack?> = attachedTo.get(RagiumDataComponents.MACHINE_UPGRADES) ?: listOf()

    override fun getUpgrades(): List<ImmutableItemStack> = getComponent().filterNotNull()

    override fun isValidUpgrade(upgrade: ImmutableItemStack, existing: List<ImmutableItemStack>): Boolean {
        val upgradeData: HTUpgradeData = RagiumDataMapTypes.getUpgradeData(upgrade) ?: return false
        val isTarget: Boolean = upgradeData.isTarget(attachedTo)
        val isCompatible: Boolean = existing.all { stack: ImmutableItemStack -> HTUpgradeData.areCompatible(upgrade, stack) }
        return isTarget && isCompatible
    }
}
