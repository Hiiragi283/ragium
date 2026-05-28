package hiiragi283.ragium.common.gui.widget

import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.impl.gui.widget.HTAbstractWidget
import hiiragi283.ragium.setup.RagiumWidgetTypes

class HTEnergySlotWidget(
    view: HTEnergyBattery,
    x: Int,
    y: Int,
) : HTAbstractWidget(x, y, 18, 18),
    HTAmountView by view {
    override fun getType(): HTWidgetType<HTEnergySlotWidget> = RagiumWidgetTypes.ENERGY_SLOT.get()
}
