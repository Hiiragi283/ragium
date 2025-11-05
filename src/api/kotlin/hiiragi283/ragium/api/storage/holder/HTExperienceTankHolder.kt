package hiiragi283.ragium.api.storage.holder

import hiiragi283.ragium.api.storage.experience.HTExperienceTank
import net.minecraft.core.Direction

/**
 * [HTExperienceTank]向けの[HTCapabilityHolder]の拡張インターフェース
 * @see mekanism.common.capabilities.holder.slot.IInventorySlotHolder
 */
interface HTExperienceTankHolder : HTCapabilityHolder {
    /**
     * 指定された[side]から[HTExperienceTank]の一覧を返します。
     */
    fun getExperienceTank(side: Direction?): List<HTExperienceTank>
}
