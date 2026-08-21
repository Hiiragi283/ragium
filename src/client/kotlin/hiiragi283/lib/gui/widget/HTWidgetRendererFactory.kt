package hiiragi283.lib.gui.widget

import hiiragi283.lib.gui.HTGuiAccess
import net.minecraft.client.gui.components.Renderable

/**
 * [HTWidget]から[Renderable]を作成するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun interface HTWidgetRendererFactory<WIDGET : HTWidget, RENDERER : Renderable> {
    fun createRenderer(gui: HTGuiAccess, widget: WIDGET): RENDERER
}
