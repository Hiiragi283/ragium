package hiiragi283.lib.transfer.holder

import hiiragi283.lib.transfer.HTResourceSlot
import net.minecraft.core.Direction

/**
 * [HTResourceSlot]向けの[HTCapabilityHolder]の拡張インターフェースです。
 *
 * 参照 : [Mekanism - IContainerHolder](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/capabilities/holder/IContainerHolder.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTResourceSlotHolder<SLOT> : HTCapabilityHolder {
    fun getSlots(side: Direction?): List<SLOT>
}
