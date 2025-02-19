package hiiragi283.ragium.client.screen

import hiiragi283.ragium.api.screen.HTContainerScreen
import hiiragi283.ragium.common.inventory.HTPotionBundleContainerMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class HTPotionBundleContainer(menu: HTPotionBundleContainerMenu, inventory: Inventory, title: Component) :
    HTContainerScreen<HTPotionBundleContainerMenu>(menu, inventory, title) {
    init {
        imageHeight = 133
        inventoryLabelY = imageHeight - 94
    }

    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        guiGraphics.blit(
            ResourceLocation.withDefaultNamespace("textures/gui/container/hopper.png"),
            startX,
            startY,
            0,
            0,
            imageWidth,
            imageHeight,
        )
    }
}
