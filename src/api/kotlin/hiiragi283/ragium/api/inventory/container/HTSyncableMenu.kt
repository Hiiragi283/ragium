package hiiragi283.ragium.api.inventory.container

import hiiragi283.ragium.api.inventory.slot.HTSyncableSlot

fun interface HTSyncableMenu {
    fun getTrackedSlot(index: Int): HTSyncableSlot?
}
