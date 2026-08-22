package hiiragi283.ragium.gui.widget

import hiiragi283.lib.gui.widget.HTAbstractWidget
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.lib.transfer.energy.HTEnergyHandler

class HTEnergySlotWidget(
    handler: HTEnergyHandler,
    x: Int,
    y: Int,
) : HTAbstractWidget(x, y, 18, 18),
    HTEnergyHandler by handler {
    override fun getType(): HTWidgetType<HTEnergySlotWidget> = RagiumWidgetTypes.ENERGY

    override fun toString(): String = "HTEnergySlotWidget(bounds=$bounds, amount=$amount, capacity=$capacity)"
}
