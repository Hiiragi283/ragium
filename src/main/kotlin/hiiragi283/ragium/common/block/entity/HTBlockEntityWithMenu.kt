package hiiragi283.ragium.common.block.entity

import hiiragi283.lib.gui.widget.HTWidgetHolder

fun interface HTBlockEntityWithMenu {
    fun setupMenu(widgetHolder: HTWidgetHolder)
}
