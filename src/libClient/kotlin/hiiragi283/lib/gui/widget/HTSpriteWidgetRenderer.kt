package hiiragi283.lib.gui.widget

import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.gui.HTSpriteRenderHelper
import hiiragi283.lib.text.Text
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import kotlin.math.roundToInt

abstract class HTSpriteWidgetRenderer<WIDGET : HTWidget>(gui: HTGuiAccess, widget: WIDGET) : HTAbstractWidgetRenderer<WIDGET>(gui, widget) {
    override fun render(bounds: HTBounds, graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        // Render background
        HTSpriteRenderHelper.blit(graphics, getBackgroundId(), bounds)
        // Render sprite
        renderSprite(bounds, graphics)
        // Render tooltip
        renderTooltips(bounds, graphics, mouseX, mouseY) { context: Item.TooltipContext, flag: TooltipFlag ->
            buildList { collectTooltips(this::add, context, flag) }
        }
    }

    private fun renderSprite(bounds: HTBounds, graphics: GuiGraphicsExtractor) {
        if (!shouldRender()) return
        var (x: Int, y: Int, width: Int, height: Int) = bounds
        x++
        y++
        width -= 2
        height -= 2

        val fixedY: Int = (height * getFilledLevel().coerceAtMost(1f)).roundToInt()
        graphics.blitSprite(
            RenderPipelines.GUI_TEXTURED,
            getSpriteId(),
            width,
            height,
            0,
            height - fixedY,
            x,
            y + height - fixedY,
            width,
            fixedY,
        )
    }

    protected abstract fun getBackgroundId(): Identifier

    protected abstract fun shouldRender(): Boolean

    protected abstract fun getFilledLevel(): Float

    protected abstract fun getSpriteId(): Identifier

    protected abstract fun collectTooltips(consumer: (Text) -> Unit, context: Item.TooltipContext, flag: TooltipFlag)
}
