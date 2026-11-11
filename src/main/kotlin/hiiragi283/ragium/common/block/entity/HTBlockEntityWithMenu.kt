package hiiragi283.ragium.common.block.entity

import hiiragi283.lib.gui.sync.HTFluidSyncSlot
import hiiragi283.lib.gui.sync.HTIntSyncSlot
import hiiragi283.lib.gui.sync.HTItemSyncSlot
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.transfer.energy.HTBasicEnergyStorage
import hiiragi283.lib.transfer.fluid.HTFluidStackTank
import hiiragi283.lib.transfer.item.HTItemStackSlot

fun interface HTBlockEntityWithMenu {
    fun setupMenu(widgetHolder: HTWidgetHolder)

    fun HTWidgetHolder.track(tank: HTFluidStackTank) {
        this += HTFluidSyncSlot(tank)
    }

    fun HTWidgetHolder.track(slot: HTItemStackSlot) {
        this += HTItemSyncSlot(slot)
    }

    fun HTWidgetHolder.track(handler: HTBasicEnergyStorage) {
        this += HTIntSyncSlot.create(handler::amount, handler::setAmount)
    }
}
