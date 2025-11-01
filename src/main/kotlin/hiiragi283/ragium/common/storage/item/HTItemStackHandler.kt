package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.core.Direction

open class HTItemStackHandler(protected var slots: List<HTItemSlot>) : HTItemHandler {
    override fun getItemSlots(side: Direction?): List<HTItemSlot> = slots
}
