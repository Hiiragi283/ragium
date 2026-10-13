package hiiragi283.ragium.common.gui.widget

import hiiragi283.lib.gui.widget.HTAbstractWidget
import hiiragi283.lib.gui.widget.HTWidgetType
import hiiragi283.lib.transfer.energy.HTEnergyHandler

class HTEnergySlotWidget(handler: HTEnergyHandler, x: Int, y: Int) :
    HTAbstractWidget(x, y, 18, 18),
    HTEnergyHandler by handler {
    override fun getType(): HTWidgetType<HTEnergySlotWidget> = RagiumWidgetTypes.ENERGY.get()

    override fun toString(): String =
        "HTEnergySlotWidget(bounds=$bounds, amount=$amountAsLong, capacity=$capacityAsLong)"

    @Suppress("NonExtendableApiUsage")
    override fun getAmountAsInt(): Int = super.getAmountAsInt()

    @Suppress("NonExtendableApiUsage")
    override fun getCapacityAsInt(): Int = super.getCapacityAsInt()
}
