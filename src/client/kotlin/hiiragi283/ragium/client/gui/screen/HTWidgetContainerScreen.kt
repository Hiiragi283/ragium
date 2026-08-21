package hiiragi283.ragium.client.gui.screen

import hiiragi283.lib.HTConstants
import hiiragi283.lib.text.Text
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.gui.widget.HTGuiWidget
import hiiragi283.ragium.gui.menu.HTWidgetContainerMenu
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory

class HTWidgetContainerScreen(menu: HTWidgetContainerMenu, inventory: Inventory, title: Text) : HTContainerScreen<HTWidgetContainerMenu>(menu, inventory, title, 176, 144 + menu.widgetHolder.rows * 18) {
    companion object {
        @JvmField
        val BACKGROUND: Identifier = RagiumAPI.id(HTConstants.TEXTURES, HTConstants.GUI, "background.png")
    }

    private val rows: Int = menu.widgetHolder.rows

    init {
        inventoryLabelY = imageHeight - 125
    }

    override fun init() {
        super.init()
        titleLabelX = (imageWidth - font.width(title)) / 2
        menu.widgetHolder.map { HTGuiWidget(this, it) }.forEach(::addRenderableWidget)
    }

    override fun extractBackground(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        super.extractBackground(graphics, mouseX, mouseY, a)
        val slotHeight: Int = 18 * rows + 17
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND, startX, startY, 0, 0, imageWidth, slotHeight, 256, 256)
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND, startX, startY + slotHeight, 0, 126, imageWidth, 96, 256, 256)
    }
}
