package hiiragi283.ragium.client.gui.widget

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.gui.HTSpriteRenderHelper
import hiiragi283.lib.gui.widget.HTAbstractWidgetRenderer
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.gui.widget.HTSolarWidget
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier

class HTSolarWidgetRenderer(gui: HTGuiAccess, widget: HTSolarWidget) : HTAbstractWidgetRenderer<HTSolarWidget>(gui, widget) {
    companion object {
        @JvmField
        val SPRITE: Identifier = RagiumAPI.id("solar")

        @JvmField
        val BACKGROUND: Identifier = RagiumAPI.id(HTConstants.TEXTURES, HTConstants.GUI, HTConstants.BACKGROUND, "solar")
    }

    override fun render(bounds: HTBounds, graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        // Render background
        HTSpriteRenderHelper.blit(graphics, BACKGROUND, bounds)
        // Render front
        val power: Double = widget.power
        val (x: Int, y: Int, width: Int, height: Int) = bounds
        val fixedY: Int = (height * power).toInt()
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE, width, height, 0, height - fixedY, x, y + height - fixedY, width, fixedY)
        // Render tooltip
        renderTooltips(bounds, graphics, mouseX, mouseY) { _, _ ->
            buildList { this += HTCommonTranslation.SOLAR_POWER.translate(ChatFormatting.AQUA, power) }
        }
    }
}
