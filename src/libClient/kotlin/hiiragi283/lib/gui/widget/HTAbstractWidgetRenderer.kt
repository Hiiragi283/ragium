package hiiragi283.lib.gui.widget

import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.gui.HTGuiRenderHelper
import hiiragi283.lib.text.Text
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Renderable
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag

/**
 * [HTWidget]向けの[Renderable]の抽象クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTAbstractWidgetRenderer<WIDGET : HTWidget>(protected val gui: HTGuiAccess, protected val widget: WIDGET) : Renderable {
    final override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        val bounds: HTBounds = widget.bounds.offset(gui.getLeftPos(), gui.getTopPos())
        render(bounds, graphics, mouseX, mouseY, a)
    }

    protected abstract fun render(bounds: HTBounds, graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float)

    /**
     * @since 26.1.11
     */
    protected inline fun renderTooltips(
        bounds: HTBounds,
        graphics: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int,
        action: (context: Item.TooltipContext, flag: TooltipFlag) -> List<Text>,
    ) {
        if (bounds.contains(mouseX, mouseY)) {
            graphics.setTooltipForNextFrame(
                Minecraft.getInstance().font,
                action(
                    HTGuiRenderHelper.getTooltipContext(),
                    HTGuiRenderHelper.getTooltipFlag(),
                ).map(Text::getVisualOrderText),
                mouseX,
                mouseY,
            )
        }
    }
}
