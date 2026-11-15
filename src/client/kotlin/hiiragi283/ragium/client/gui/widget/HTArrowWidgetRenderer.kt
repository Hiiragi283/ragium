package hiiragi283.ragium.client.gui.widget

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.gui.HTSpriteRenderHelper
import hiiragi283.lib.gui.widget.HTAbstractWidgetRenderer
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.gui.widget.HTArrowWidget
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import kotlin.math.roundToInt

sealed class HTArrowWidgetRenderer<WIDGET : HTArrowWidget>(
    gui: HTGuiAccess,
    widget: WIDGET,
    protected val sprite: Identifier,
    private val background: Identifier,
) : HTAbstractWidgetRenderer<WIDGET>(gui, widget) {
    protected val progress: Float get() = widget.progress

    override fun render(bounds: HTBounds, graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        // Render background
        HTSpriteRenderHelper.blit(graphics, background, bounds)
        // Render sprite
        if (progress > 0) {
            renderArrow(bounds, graphics, mouseX, mouseY, partialTick)
        }
    }

    protected abstract fun renderArrow(bounds: HTBounds, graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float)

    class Right(gui: HTGuiAccess, widget: HTArrowWidget.Right) :
        HTArrowWidgetRenderer<HTArrowWidget.Right>(
            gui,
            widget,
            RagiumAPI.id("arrow_right"),
            RagiumAPI.id(HTConstants.TEXTURES, HTConstants.GUI, HTConstants.BACKGROUND, "arrow_right"),
        ) {
        override fun renderArrow(bounds: HTBounds, graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
            val (x: Int, y: Int, width: Int, height: Int) = bounds

            val fixedX: Int = (width * progress.coerceAtMost(1f)).roundToInt()
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, width, height, 0, 0, x, y, fixedX, height)
        }
    }

    class Left(gui: HTGuiAccess, widget: HTArrowWidget.Left) :
        HTArrowWidgetRenderer<HTArrowWidget.Left>(
            gui,
            widget,
            RagiumAPI.id("arrow_left"),
            RagiumAPI.id(HTConstants.TEXTURES, HTConstants.GUI, HTConstants.BACKGROUND, "arrow_left"),
        ) {
        override fun renderArrow(bounds: HTBounds, graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
            val (x: Int, y: Int, width: Int, height: Int) = bounds

            val fixedX: Int = (width * progress.coerceAtMost(1f)).roundToInt()
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, width, height, width - fixedX, 0, x + width - fixedX, y, fixedX, height)
        }
    }
}
