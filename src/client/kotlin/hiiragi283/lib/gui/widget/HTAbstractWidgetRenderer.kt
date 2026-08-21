package hiiragi283.lib.gui.widget

import hiiragi283.lib.gui.HTBounds
import hiiragi283.lib.gui.HTGuiAccess
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Renderable

/**
 * [HTWidget]向けの[Renderable]の抽象クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTAbstractWidgetRenderer<WIDGET : HTWidget>(protected val gui: HTGuiAccess, protected val widget: WIDGET) : Renderable {
    final override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, a: Float) {
        val bounds: HTBounds = widget.bounds.offset(gui.getGuiLeft(), gui.getGuiTop())
        render(bounds, graphics, mouseX, mouseY, a)
    }

    protected abstract fun render(
        bounds: HTBounds,
        graphics: GuiGraphicsExtractor,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    )
}
