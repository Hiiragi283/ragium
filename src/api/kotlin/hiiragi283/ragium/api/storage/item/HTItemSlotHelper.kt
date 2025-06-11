package hiiragi283.ragium.api.storage.item

import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity

object HTItemSlotHelper {
    @JvmStatic
    fun insertItem(slots: Iterable<HTItemSlot>, stack: ItemStack, simulate: Boolean): ItemStack {
        val result: ItemStack = if (simulate) stack.copy() else stack
        for (slot: HTItemSlot in slots) {
            slot.insert(result, simulate)
            if (result.isEmpty) return ItemStack.EMPTY
        }
        return result
    }

    @JvmStatic
    fun consumeItem(slot: HTItemSlot, count: Int, player: Player?) {
        val stack: ItemStack = slot.stack
        if (stack.hasCraftingRemainingItem()) {
            slot.replace(stack.craftingRemainingItem, true)
        } else {
            if (player != null) {
                stack.consume(count, player)
                slot.replace(stack, true)
            } else {
                stack.shrink(count)
            }
        }
    }

    @JvmStatic
    fun createSlotList(size: Int, prefix: String, blockEntity: BlockEntity): List<HTItemSlot> = (0 until size).map { index: Int ->
        HTItemSlot.create(prefix + "_$index", blockEntity)
    }

    @JvmStatic
    fun createEmptySlotList(size: Int): List<HTItemSlot> = (0 until size).map { _: Int -> HTItemSlot.create("") }
}
