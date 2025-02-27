package hiiragi283.ragium.api.storage

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandlerModifiable

interface HTItemSlotHandler : IItemHandlerModifiable {
    fun getItemIoFromSlot(slot: Int): HTStorageIO

    fun getItemSlot(slot: Int): HTItemSlot?

    //    IItemHandlerModifiable    //

    override fun setStackInSlot(slot: Int, stack: ItemStack) {
        getItemSlot(slot)?.setStack(stack)
    }

    override fun getStackInSlot(slot: Int): ItemStack = getItemSlot(slot)?.getStack() ?: ItemStack.EMPTY

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (!getItemIoFromSlot(slot).canInsert) return stack
        return getItemSlot(slot)?.insertItem(stack, simulate) ?: stack
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (!getItemIoFromSlot(slot).canExtract) return ItemStack.EMPTY
        return getItemSlot(slot)?.extractItem(amount, simulate) ?: ItemStack.EMPTY
    }

    override fun getSlotLimit(slot: Int): Int {
        val slotIn: HTItemSlot = getItemSlot(slot) ?: return 0
        return slotIn.getMaxSize(slotIn.getStack())
    }

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = getItemSlot(slot)?.isItemValid(stack) == true

    //    Empty    //

    interface Empty : HTItemSlotHandler {
        override fun getItemIoFromSlot(slot: Int): HTStorageIO = HTStorageIO.EMPTY

        override fun getItemSlot(slot: Int): HTItemSlot? = null

        override fun getSlots(): Int = 0
    }
}
