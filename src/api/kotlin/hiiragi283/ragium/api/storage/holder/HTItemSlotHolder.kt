package hiiragi283.ragium.api.storage.holder

import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.core.Direction

/**
 * [HTItemSlot]向けの[HTCapabilityHolder]の拡張インターフェース
 * @see [mekanism.common.capabilities.holder.slot.IInventorySlotHolder]
 */
interface HTItemSlotHolder : HTCapabilityHolder {
    /**
     * 指定された[side]から[HTItemSlot]の一覧を返します。
     */
    fun getItemSlot(side: Direction?): List<HTItemSlot>
}
