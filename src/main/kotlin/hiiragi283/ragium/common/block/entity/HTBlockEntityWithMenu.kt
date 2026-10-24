package hiiragi283.ragium.common.block.entity

import hiiragi283.lib.gui.sync.HTFluidSyncSlot
import hiiragi283.lib.gui.sync.HTIntSyncSlot
import hiiragi283.lib.gui.sync.HTItemSyncSlot
import hiiragi283.lib.gui.sync.HTSyncType
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.transfer.energy.HTBasicEnergyHandler
import hiiragi283.lib.transfer.fluid.HTFluidStackResourceSlot
import hiiragi283.lib.transfer.item.HTItemStackResourceSlot

fun interface HTBlockEntityWithMenu {
    fun setupMenu(widgetHolder: HTWidgetHolder)

    fun HTWidgetHolder.track(tank: HTFluidStackResourceSlot, syncType: HTSyncType = HTSyncType.S2C) {
        this.track(HTFluidSyncSlot(tank), syncType)
    }

    fun HTWidgetHolder.track(slot: HTItemStackResourceSlot, syncType: HTSyncType = HTSyncType.S2C) {
        this.track(HTItemSyncSlot(slot), syncType)
    }

    fun HTWidgetHolder.track(handler: HTBasicEnergyHandler, syncType: HTSyncType = HTSyncType.S2C) {
        this.track(HTIntSyncSlot.create(handler::getAmountAsInt, handler::setAmount), syncType)
    }
}
