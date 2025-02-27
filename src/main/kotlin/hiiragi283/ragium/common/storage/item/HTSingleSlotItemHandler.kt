package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.storage.HTItemSlot
import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandlerModifiable

class HTSingleSlotItemHandler(val storageIO: HTStorageIO, val itemSlot: HTItemSlot) : IItemHandlerModifiable {
    override fun setStackInSlot(slot: Int, stack: ItemStack) {
        itemSlot.setStack(stack)
    }

    override fun getSlots(): Int = 1

    override fun getStackInSlot(slot: Int): ItemStack = itemSlot.getStack()

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (!storageIO.canInsert) return stack
        return itemSlot.insertItem(stack, simulate)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (!storageIO.canExtract) return ItemStack.EMPTY
        return itemSlot.extractItem(amount, simulate)
    }

    override fun getSlotLimit(slot: Int): Int = itemSlot.getMaxSize(itemSlot.getStack())

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = itemSlot.isItemValid(stack)
}
