package hiiragi283.ragium.api.capability

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.upgrade.HTUpgradeHandler
import hiiragi283.ragium.api.upgrade.HTUpgradeProvider
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.ItemCapability

object RagiumCapabilities {
    @JvmField
    val UPGRADABLE_BLOCK: BlockCapability<HTUpgradeHandler, Void?> = BlockCapability.createVoid(
        RagiumAPI.id("upgradable"),
        HTUpgradeHandler::class.java,
    )

    @JvmField
    val UPGRADABLE_ITEM: ItemCapability<HTUpgradeHandler, Void?> = ItemCapability.createVoid(
        RagiumAPI.id("upgradable"),
        HTUpgradeHandler::class.java,
    )

    @JvmField
    val UPGRADE_ITEM: ItemCapability<HTUpgradeProvider, Void?> = ItemCapability.createVoid(
        RagiumAPI.id("upgrade"),
        HTUpgradeProvider::class.java,
    )
}
