package hiiragi283.ragium.common.upgrade

import hiiragi283.ragium.api.capability.RagiumCapabilities
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.upgrade.HTUpgradeGroup
import hiiragi283.ragium.api.upgrade.HTUpgradeHandler
import hiiragi283.ragium.api.upgrade.HTUpgradeProvider
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.world.item.ItemStack

class HTComponentUpgradeHandler(private val parent: ItemStack) : HTUpgradeHandler {
    private fun getComponent(): Collection<ImmutableItemStack?> = parent.get(RagiumDataComponents.MACHINE_UPGRADES) ?: listOf()

    override fun getUpgrades(): List<ImmutableItemStack> = getComponent().filterNotNull()

    override fun isValidUpgrade(upgrade: ImmutableItemStack, existing: List<ImmutableItemStack>): Boolean {
        val provider: HTUpgradeProvider = upgrade.getCapability(RagiumCapabilities.UPGRADE_ITEM) ?: return false
        val group: HTUpgradeGroup? = provider.getGroup()
        val attachedGroup: HTUpgradeGroup = parent.get(RagiumDataComponents.UPGRADE_GROUP) ?: return true
        return when (group) {
            attachedGroup -> existing.none { stack: ImmutableItemStack ->
                stack.getCapability(RagiumCapabilities.UPGRADE_ITEM)?.getGroup() == group
            }
            else -> false
        }
    }
}
