package hiiragi283.ragium.common.upgrade

import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.upgrade.HTUpgradeHandler
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.world.item.ItemStack

class HTComponentUpgradeHandler(private val parent: ItemStack) : HTUpgradeHandler {
    private fun getComponent(): Collection<ImmutableItemStack?> = parent.get(RagiumDataComponents.MACHINE_UPGRADES) ?: listOf()

    override fun getUpgrades(): List<ImmutableItemStack> = getComponent().filterNotNull()

    override fun isValidUpgrade(upgrade: ImmutableItemStack, existing: List<ImmutableItemStack>): Boolean =
        existing.none { stack: ImmutableItemStack -> ItemStack.isSameItemSameComponents(stack.unwrap(), upgrade.unwrap()) }
}
