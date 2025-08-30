package hiiragi283.ragium.api.inventory.slot

import net.minecraft.world.item.ItemStack

/**
 * 搬入可能なスロットかどうか判定する関数型インターフェース
 * @see [mekanism.common.inventory.container.slot.IInsertableSlot]
 */
fun interface HTSlot {
    fun insertItem(stack: ItemStack, simulate: Boolean): ItemStack
}
