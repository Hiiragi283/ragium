package hiiragi283.ragium.api.storage.item

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler

class HTFilteredItemHandler(private val delegate: IItemHandler, private val filter: HTItemFilter) : IItemHandler {
    override fun getSlots(): Int = delegate.slots

    override fun getStackInSlot(slot: Int): ItemStack = delegate.getStackInSlot(slot)

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (!filter.canInsert(delegate, slot, stack)) return stack
        return delegate.insertItem(slot, stack, simulate)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (!filter.canExtract(delegate, slot, amount)) return ItemStack.EMPTY
        return delegate.extractItem(slot, amount, simulate)
    }

    override fun getSlotLimit(slot: Int): Int = delegate.getSlotLimit(slot)

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = delegate.isItemValid(slot, stack)
}
