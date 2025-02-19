package hiiragi283.ragium.common.item.capability

import hiiragi283.ragium.api.capability.HTStorageIO
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandlerModifiable

class HTLimitedItemHandler(val storageIO: HTStorageIO, val delegated: IItemHandlerModifiable) : IItemHandlerModifiable {
    override fun getSlots(): Int = delegated.slots

    override fun getStackInSlot(slot: Int): ItemStack = delegated.getStackInSlot(slot)

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (!storageIO.canInsert) return stack
        return delegated.insertItem(slot, stack, simulate)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (!storageIO.canExtract) return ItemStack.EMPTY
        return delegated.extractItem(slot, amount, simulate)
    }

    override fun getSlotLimit(slot: Int): Int = delegated.getSlotLimit(slot)

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = delegated.isItemValid(slot, stack)

    override fun setStackInSlot(slot: Int, stack: ItemStack) {
        delegated.setStackInSlot(slot, stack)
    }
}
