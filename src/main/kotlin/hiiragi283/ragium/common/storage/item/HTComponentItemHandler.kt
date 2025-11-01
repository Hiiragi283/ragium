package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.core.Direction

/**
 * [HTItemHandler]に基づいたコンポーネント向けの実装
 * @see mekanism.common.attachments.containers.item.ComponentBackedItemHandler
 */
class HTComponentItemHandler(private val slots: List<HTItemSlot>) : HTItemHandler {
    override fun getItemSlots(side: Direction?): List<HTItemSlot> = slots
}
