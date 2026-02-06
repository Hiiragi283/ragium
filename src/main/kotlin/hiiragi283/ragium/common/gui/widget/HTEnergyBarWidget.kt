package hiiragi283.ragium.common.gui.widget

import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.api.gui.widget.HTAbstractWidget
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.storage.amount.HTAmountView
import hiiragi283.core.api.storage.energy.HTEnergyBattery
import hiiragi283.core.common.gui.sync.HTIntSyncSlot
import hiiragi283.ragium.setup.RagiumWidgetTypes

class HTEnergyBarWidget(
    private val view: HTEnergyBattery,
    private val syncSlot: HTIntSyncSlot,
    x: Int,
    y: Int,
) : HTAbstractWidget(x, y, 7, 18 * 3 - 2),
    HTAmountView by view {
    constructor(view: HTEnergyBattery.Basic, x: Int, y: Int) : this(view, HTIntSyncSlot.create(view), x, y)

    override fun getType(): HTWidgetType<HTEnergyBarWidget> = RagiumWidgetTypes.ENERGY_BAR.get()

    override fun setupHolder(widgetHolder: HTWidgetHolder) {
        widgetHolder.track(syncSlot, HTSyncType.S2C)
    }
}
