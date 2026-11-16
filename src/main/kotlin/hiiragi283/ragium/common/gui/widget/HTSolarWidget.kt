package hiiragi283.ragium.common.gui.widget

import hiiragi283.lib.gui.widget.HTAbstractWidget
import hiiragi283.lib.gui.widget.HTWidgetType

class HTSolarWidget(private val provider: () -> Double, x: Int, y: Int) : HTAbstractWidget(x, y, 18, 18) {
    val power: Double get() = provider().coerceIn(0.0, 1.0)

    override fun getType(): HTWidgetType<*> = RagiumWidgetTypes.SOLAR.get()
}
