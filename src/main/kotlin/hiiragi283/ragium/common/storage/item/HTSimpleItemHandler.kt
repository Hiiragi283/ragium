package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.inventory.HTSlotHelper
import net.minecraft.core.Direction

open class HTSimpleItemHandler(protected var slots: List<HTItemSlot>) : HTItemHandler {
    companion object {
        @JvmStatic
        inline fun createSlots(rows: Int, factory: (Int, Int) -> HTItemSlot): List<HTItemSlot> = (0..<(rows * 9)).map { index: Int ->
            factory(HTSlotHelper.getSlotPosX(index % 9), HTSlotHelper.getSlotPosY(index / 9))
        }

        @JvmStatic
        inline fun create(rows: Int, factory: (Int, Int) -> HTItemSlot): HTItemHandler =
            createSlots(rows, factory).let(::HTSimpleItemHandler)
    }

    final override fun getItemSlots(side: Direction?): List<HTItemSlot> = slots
}
