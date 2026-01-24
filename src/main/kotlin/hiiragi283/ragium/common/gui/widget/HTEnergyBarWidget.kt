package hiiragi283.ragium.common.gui.widget

import hiiragi283.core.api.gui.widget.HTAbstractWidget
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.setup.RagiumWidgetTypes

class HTEnergyBarWidget(private val view: HTEnergyBattery, x: Int, y: Int) :
    HTAbstractWidget<HTEnergyBarWidget>(x, y, 7, 18 * 3 - 2),
    HTAmountView by view {
    override fun getType(): HTWidgetType<HTEnergyBarWidget> = RagiumWidgetTypes.ENERGY_BAR.get()
}
