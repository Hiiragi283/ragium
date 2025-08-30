package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.gui.screen.HTContainerScreen
import hiiragi283.ragium.api.inventory.container.HTBaseGenericContainerMenu
import hiiragi283.ragium.api.inventory.container.HTContainerMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

/**
 * @see [net.minecraft.client.gui.screens.inventory.ContainerScreen]
 */
class HTGenericScreen<MENU>(menu: MENU, inventory: Inventory, title: Component) :
    HTContainerScreen<MENU>(menu, inventory, title) where MENU : HTContainerMenu, MENU : HTBaseGenericContainerMenu {
    companion object {
        @JvmStatic
        private val TEXTURE_ID: ResourceLocation = vanillaId("textures/gui/container/generic_54.png")
    }

    override val texture: ResourceLocation? = null

    val rows: Int = menu.rows

    init {
        imageHeight = 114 + rows * 18
        inventoryLabelY = imageHeight - 94
    }

    override fun renderBg(
        guiGraphics: GuiGraphics,
        partialTick: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        guiGraphics.blit(TEXTURE_ID, startX, startY, 0, 0, imageWidth, rows * 18 + 17)
        guiGraphics.blit(TEXTURE_ID, startX, startY + rows * 18 + 17, 0, 126, imageWidth, 96)
    }
}
