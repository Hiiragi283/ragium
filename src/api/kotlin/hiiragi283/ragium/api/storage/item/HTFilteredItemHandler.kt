package hiiragi283.ragium.api.storage.item

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler

class HTFilteredItemHandler(private val delegate: IItemHandler, val inputSlots: IntArray, val outputSlots: IntArray) : IItemHandler {
    override fun getSlots(): Int = delegate.slots

    override fun getStackInSlot(slot: Int): ItemStack = delegate.getStackInSlot(slot)

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = when (slot) {
        !in inputSlots -> stack
        else -> delegate.insertItem(slot, stack, simulate)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = when (slot) {
        !in outputSlots -> ItemStack.EMPTY
        else -> delegate.extractItem(slot, amount, simulate)
    }

    override fun getSlotLimit(slot: Int): Int = delegate.getSlotLimit(slot)

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = delegate.isItemValid(slot, stack)
}
