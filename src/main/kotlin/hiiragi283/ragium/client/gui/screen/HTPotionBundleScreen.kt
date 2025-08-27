package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.screen.HTContainerScreen
import hiiragi283.ragium.common.inventory.container.HTPotionBundleMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class HTPotionBundleScreen(menu: HTPotionBundleMenu, inventory: Inventory, title: Component) :
    HTContainerScreen<HTPotionBundleMenu>(menu, inventory, title) {
    init {
        imageHeight = 133
        inventoryLabelY = imageHeight - 94
    }

    override val texture: ResourceLocation = RagiumAPI.id("textures/gui/container/potion_bundle.png")
}
