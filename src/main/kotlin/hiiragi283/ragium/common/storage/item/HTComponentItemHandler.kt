package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.core.Direction

/**
 * [HTItemHandler]に基づいたコンポーネント向けの実装
 */
class HTComponentItemHandler(private val slots: List<HTItemSlot>) : HTItemHandler {
    constructor(vararg slots: HTItemSlot) : this(slots.toList())

    constructor(size: Int, factory: (Int) -> HTItemSlot) : this((0..<size).map(factory))

    override fun getItemSlots(side: Direction?): List<HTItemSlot> = slots
}
