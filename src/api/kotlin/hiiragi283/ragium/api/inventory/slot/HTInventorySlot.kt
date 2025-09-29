package hiiragi283.ragium.api.inventory.slot

import net.minecraft.world.Container
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import kotlin.math.min

/**
 * プレイヤーのインベントリのスロットに用いられる[HTSlot]の実装
 * @see [HTHotBarSlot]
 * @see [HTMainInventorySlot]
 * @see [mekanism.common.inventory.container.slot.InsertableSlot]
 */
open class HTInventorySlot(
    container: Container,
    slot: Int,
    x: Int,
    y: Int,
) : Slot(container, slot, x, y),
    HTSlot {
    override fun insertItem(stack: ItemStack, simulate: Boolean): ItemStack {
        if (stack.isEmpty || !mayPlace(stack)) return stack
        val current: ItemStack = item
        val needed: Int = getMaxStackSize(stack) - current.count
        if (needed <= 0) return stack
        if (current.isEmpty || ItemStack.isSameItemSameComponents(current, stack)) {
            val toAdd: Int = min(stack.count, needed)
            if (!simulate) {
                set(stack.copyWithCount(current.count + toAdd))
            }
            return stack.copyWithCount(current.count - toAdd)
        }
        return stack
    }
}
