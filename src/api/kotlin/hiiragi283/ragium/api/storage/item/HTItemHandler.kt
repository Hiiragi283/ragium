package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack

/**
 * [HTItemSlot]に基づいた[HTSidedItemHandler]の拡張インターフェース
 * @see [mekanism.api.inventory.IMekanismInventory]
 */
interface HTItemHandler :
    HTSidedItemHandler,
    HTContentListener {
    fun getItemSlots(side: Direction?): List<HTItemSlot>

    fun getItemSlot(slot: Int, side: Direction?): HTItemSlot? = getItemSlots(side).getOrNull(slot)

    override fun setStackInSlot(slot: Int, stack: ItemStack, side: Direction?) {
        getItemSlot(slot, side)?.setStack(stack)
    }

    override fun getStackInSlot(slot: Int, side: Direction?): ItemStack = getItemSlot(slot, side)?.getStack() ?: ItemStack.EMPTY

    override fun getSlots(side: Direction?): Int = getItemSlots(side).size

    override fun insertItem(
        slot: Int,
        stack: ItemStack,
        side: Direction?,
        simulate: Boolean,
    ): ItemStack = getItemSlot(slot, side)?.insertItem(stack, simulate, HTStorageAccess.forHandler(side)) ?: stack

    override fun extractItem(
        slot: Int,
        amount: Int,
        side: Direction?,
        simulate: Boolean,
    ): ItemStack = getItemSlot(slot, side)?.extractItem(amount, simulate, HTStorageAccess.forHandler(side)) ?: ItemStack.EMPTY

    override fun getSlotLimit(slot: Int, side: Direction?): Int = getItemSlot(slot, side)?.getLimit(ItemStack.EMPTY) ?: 0

    override fun isItemValid(slot: Int, stack: ItemStack, side: Direction?): Boolean = getItemSlot(slot, side)?.isItemValid(stack) ?: false
}
