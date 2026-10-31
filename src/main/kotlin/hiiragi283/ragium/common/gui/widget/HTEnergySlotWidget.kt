package hiiragi283.ragium.common.gui.widget

import hiiragi283.lib.gui.widget.HTAbstractWidget
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.lib.transfer.energy.HTEnergyView

class HTEnergySlotWidget(view: HTEnergyView, x: Int, y: Int) :
    HTAbstractWidget(x, y, 18, 18),
    HTEnergyView by view {
    override fun getType(): HTWidgetType<HTEnergySlotWidget> = RagiumWidgetTypes.ENERGY.get()

    override fun toString(): String = "HTEnergySlotWidget(bounds=$bounds, amount=$amount, capacity=$capacity)"
}
