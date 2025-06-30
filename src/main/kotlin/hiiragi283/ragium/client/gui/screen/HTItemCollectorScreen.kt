package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.screen.HTContainerScreen
import hiiragi283.ragium.common.inventory.HTItemCollectorMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class HTItemCollectorScreen(menu: HTItemCollectorMenu, inventory: Inventory, title: Component) :
    HTContainerScreen<HTItemCollectorMenu>(menu, inventory, title) {
    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        guiGraphics.blit(RagiumAPI.id("textures/gui/container/item_collector.png"), startX, startY, 0, 0, imageWidth, imageHeight)
    }
}
