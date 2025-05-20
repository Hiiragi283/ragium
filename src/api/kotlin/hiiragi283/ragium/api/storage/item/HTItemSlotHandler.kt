package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandlerModifiable

interface HTItemSlotHandler : IItemHandlerModifiable {
    fun getItemIoFromSlot(slot: Int): HTStorageIO

    fun getItemSlot(slot: Int): HTItemSlot?

    //    IItemHandlerModifiable    //

    override fun setStackInSlot(slot: Int, stack: ItemStack) {
        val slot: HTItemSlot = getItemSlot(slot) ?: return
        slot.clear()
        slot.insert(stack, false)
    }

    override fun getStackInSlot(slot: Int): ItemStack = getItemSlot(slot)?.stack ?: ItemStack.EMPTY

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (!getItemIoFromSlot(slot).canInsert) return stack
        val itemSlot: HTItemSlot = getItemSlot(slot) ?: return stack
        val inserted: Int = itemSlot.insert(stack, simulate)
        if (inserted == 0) return stack
        itemSlot.onContentsChanged()
        return stack.copyWithCount(stack.count - inserted)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (!getItemIoFromSlot(slot).canExtract) return ItemStack.EMPTY
        val itemSlot: HTItemSlot = getItemSlot(slot) ?: return ItemStack.EMPTY
        val resource: HTItemVariant = itemSlot.resource
        val extracted: Int = itemSlot.extract(resource, amount, simulate)
        if (extracted == 0) return ItemStack.EMPTY
        itemSlot.onContentsChanged()
        return resource.toStack(extracted)
    }

    override fun getSlotLimit(slot: Int): Int = getItemSlot(slot)?.capacity ?: 0

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = getItemSlot(slot)?.canInsert(stack) == true
}
