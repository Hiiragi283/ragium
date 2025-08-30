package hiiragi283.ragium.common.storage.holder

import hiiragi283.ragium.api.storage.HTTransferIO
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.core.Direction

class HTSimpleItemSlotHolder(
    private val transferProvider: HTTransferIO.Provider?,
    private val inputSlots: List<HTItemSlot>,
    private val outputSlots: List<HTItemSlot>,
    private val catalyst: HTItemSlot? = null,
) : HTItemSlotHolder {
    override fun getItemSlot(side: Direction?): List<HTItemSlot> = when {
        side == null -> buildList {
            addAll(inputSlots)
            addAll(outputSlots)
            catalyst?.let(::add)
        }

        canInsert(side) -> inputSlots
        canExtract(side) -> outputSlots
        else -> listOf()
    }

    override fun canInsert(side: Direction?): Boolean = when (side) {
        null -> false
        else -> transferProvider?.apply(side)?.canInsert ?: true
    }

    override fun canExtract(side: Direction?): Boolean = when (side) {
        null -> false
        else -> transferProvider?.apply(side)?.canExtract ?: true
    }
}
