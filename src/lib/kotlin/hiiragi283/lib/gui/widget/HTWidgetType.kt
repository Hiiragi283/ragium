package hiiragi283.lib.gui.widget

import hiiragi283.lib.resource.HTIdLike
import net.minecraft.resources.Identifier

/**
 * [HTWidget]を識別するためのインターフェースです。
 * @param WIDGET [HTWidget]を実装したクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTWidgetType<WIDGET : HTWidget>(private val id: Identifier) : HTIdLike {
    override fun getId(): Identifier = id

    override fun toString(): String = "HTWidgetType(id=$id)"
}
