package hiiragi283.ragium.client.gui.widget

import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.gui.widget.HTAbstractWidgetRenderer
import hiiragi283.ragium.client.util.HTSpriteRenderHelper
import hiiragi283.ragium.common.gui.widget.HTFillDirection
import hiiragi283.ragium.common.gui.widget.HTProgressWidget
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.resources.Identifier

class HTProgressWidgetRenderer(gui: HTGuiAccess, widget: HTProgressWidget) :
    HTAbstractWidgetRenderer<HTProgressWidget>(gui, widget) {
    override fun render(
        bounds: HTBounds,
        graphics: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float
    ) {
        val background: Identifier = widget.backgroundTexture ?: return
        HTSpriteRenderHelper.blit(graphics, background, bounds)

        val texture: Identifier = widget.texture ?: return

        val progress: Float = widget.getProgress()
        val progressInverted: Float = 1 - progress
        val (x: Int, y: Int, width: Int, height: Int) = bounds

        val direction: HTFillDirection = widget.fillDirection
        val startX: Int = when (direction) {
            HTFillDirection.RIGHT_TO_LEFT -> x + (width * progressInverted).toInt()
            else -> x
        }
        val startY: Int = when (direction) {
            HTFillDirection.END_TO_TOP -> y + (height * progressInverted).toInt()
            else -> y
        }
        val widthFixed: Int = when (direction) {
            HTFillDirection.TOP_TO_END -> width
            HTFillDirection.END_TO_TOP -> width
            HTFillDirection.LEFT_TO_RIGHT -> width * progress
            HTFillDirection.RIGHT_TO_LEFT -> width * progress
        }.toInt()
        val heightFixed: Int = when (direction) {
            HTFillDirection.TOP_TO_END -> height * progress
            HTFillDirection.END_TO_TOP -> height * progress
            HTFillDirection.LEFT_TO_RIGHT -> height
            HTFillDirection.RIGHT_TO_LEFT -> height
        }.toInt()

        HTSpriteRenderHelper.blit(
            graphics,
            texture,
            startX,
            startY,
            widthFixed,
            heightFixed,
            textureWidth = width,
            textureHeight = height
        )
    }
}
