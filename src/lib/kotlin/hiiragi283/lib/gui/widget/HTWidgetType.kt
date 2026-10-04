package hiiragi283.lib.gui.widget

import net.minecraft.resources.Identifier

/**
 * [HTWidget]を識別するためのインターフェースです。
 * @param WIDGET [HTWidget]を実装したクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTWidgetType<WIDGET : HTWidget> {
    companion object {
        @JvmStatic
        fun <WIDGET : HTWidget> simple(id: Identifier): HTWidgetType<WIDGET> = object : HTWidgetType<WIDGET> {
            override fun toString(): String = id.toString()
        }
    }
}
