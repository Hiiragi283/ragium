package hiiragi283.ragium.api.block.entity

import hiiragi283.core.api.gui.widget.HTWidgetHolder

fun interface HTBlockEntityWithMenu {
    fun setupMenu(widgetHolder: HTWidgetHolder)
}
