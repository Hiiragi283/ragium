package hiiragi283.lib.gui.widget

import net.minecraft.resources.Identifier

/**
 * [HTWidget]を識別するためのインターフェースです。
 * @param WIDGET [HTWidget]を実装したクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTWidgetType<WIDGET : HTWidget>(val id: Identifier) {
    override fun toString(): String = "HTWidgetType(id=$id)"
}
