package hiiragi283.ragium.api.storage.item

import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity

object HTItemSlotHelper {
    @JvmStatic
    fun canInsertItem(slots: Iterable<HTItemSlot>, stack: ItemStack): Boolean {
        val result: ItemStack = stack.copy()
        for (slot: HTItemSlot in slots) {
            val inserted: Int = slot.insert(result, true)
            if (inserted > 0) result.count -= inserted
            if (result.isEmpty) return true
        }
        return false
    }

    @JvmStatic
    fun insertItem(slots: Iterable<HTItemSlot>, stack: ItemStack, simulate: Boolean): ItemStack {
        for (slot: HTItemSlot in slots) {
            slot.insert(stack, simulate)
            if (stack.isEmpty) return ItemStack.EMPTY
        }
        return stack
    }

    @JvmStatic
    fun consumeItem(slot: HTItemSlot, count: Int, player: Player?) {
        val stack: ItemStack = slot.stack
        if (stack.hasCraftingRemainingItem()) {
            slot.replace(stack.craftingRemainingItem, true)
        } else {
            if (player != null) {
                stack.consume(count, player)
            } else {
                stack.shrink(count)
            }
            slot.replace(stack, true)
        }
    }

    @JvmStatic
    fun createSlotList(size: Int, prefix: String, blockEntity: BlockEntity): List<HTItemSlot> = (0 until size).map { index: Int ->
        HTItemSlot.create(prefix + "_$index", blockEntity)
    }

    @JvmStatic
    fun createEmptySlotList(size: Int): List<HTItemSlot> = (0 until size).map { _: Int -> HTItemSlot.create("") }
}
