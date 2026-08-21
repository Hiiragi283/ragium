package hiiragi283.lib.client.gui.widget

import hiiragi283.lib.gui.HTGuiAccess
import hiiragi283.lib.gui.widget.HTWidget
import net.minecraft.client.gui.components.Renderable

/**
 * [hiiragi283.lib.gui.widget.HTWidget]から[Renderable]を作成するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun interface HTWidgetRendererFactory<WIDGET : HTWidget, RENDERER : Renderable> {
    fun createRenderer(gui: HTGuiAccess, widget: WIDGET): RENDERER
}
