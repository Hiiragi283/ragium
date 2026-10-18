package hiiragi283.ragium.client.gui.widget

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.gui.HTSpriteRenderHelper
import hiiragi283.lib.gui.widget.HTAbstractWidgetRenderer
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.gui.widget.HTProgressWidget
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import kotlin.math.roundToInt

class HTProgressWidgetRenderer(gui: HTGuiAccess, widget: HTProgressWidget) :
    HTAbstractWidgetRenderer<HTProgressWidget>(gui, widget) {
    companion object {
        @JvmField
        val SPRITE: Identifier = RagiumAPI.id("arrow")

        @JvmField
        val BACKGROUND: Identifier = RagiumAPI.id(HTConstants.TEXTURES, HTConstants.GUI, "arrow_background")
    }

    override fun render(
        bounds: HTBounds,
        graphics: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float
    ) {
        // Render background
        HTSpriteRenderHelper.blit(graphics, BACKGROUND, bounds)
        // Render sprite
        if (widget.progress > 0) {
            val (x: Int, y: Int, width: Int, height: Int) = bounds

            val fixedX: Int = (width * widget.progress.coerceAtMost(1f)).roundToInt()
            graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                SPRITE,
                width,
                height,
                0,
                0,
                x,
                y,
                fixedX,
                height
            )
        }
    }
}
