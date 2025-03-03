package hiiragi283.ragium.client.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.screen.HTContainerScreen
import hiiragi283.ragium.common.inventory.HTPotionBundleMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class HTPotionBundleScreen(menu: HTPotionBundleMenu, inventory: Inventory, title: Component) :
    HTContainerScreen<HTPotionBundleMenu>(menu, inventory, title) {
    init {
        imageHeight = 130
        inventoryLabelY = imageHeight - 94
    }

    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        guiGraphics.blit(
            RagiumAPI.id("textures/gui/potion_bundle.png"),
            startX,
            startY,
            0,
            0,
            imageWidth,
            imageHeight,
        )
    }
}
