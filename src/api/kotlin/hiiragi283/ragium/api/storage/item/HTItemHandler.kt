package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack

/**
 * [HTItemSlot]に基づいた[HTSidedItemHandler]の拡張インターフェース
 * @see [mekanism.api.inventory.IMekanismInventory]
 */
interface HTItemHandler :
    HTSidedItemHandler,
    HTContentListener {
    fun hasItemHandler(): Boolean = true

    fun getItemSlots(side: Direction?): List<HTItemSlot>

    fun getItemSlot(slot: Int, side: Direction?): HTItemSlot? = getItemSlots(side).getOrNull(slot)

    override fun getStackInSlot(slot: Int, side: Direction?): ItemStack = getItemSlot(slot, side)?.getItemStack() ?: ItemStack.EMPTY

    override fun getSlots(side: Direction?): Int = getItemSlots(side).size

    override fun insertItem(
        slot: Int,
        stack: ItemStack,
        action: HTStorageAction,
        side: Direction?,
    ): ItemStack = getItemSlot(slot, side)?.insertItem(stack, action, HTStorageAccess.forHandler(side)) ?: stack

    override fun extractItem(
        slot: Int,
        amount: Int,
        action: HTStorageAction,
        side: Direction?,
    ): ItemStack = getItemSlot(slot, side)?.extractItem(amount, action, HTStorageAccess.forHandler(side)) ?: ItemStack.EMPTY

    override fun getSlotLimit(slot: Int, side: Direction?): Int = getItemSlot(slot, side)?.getCapacityAsInt(ItemStack.EMPTY) ?: 0

    override fun isItemValid(slot: Int, stack: ItemStack, side: Direction?): Boolean = getItemSlot(slot, side)?.isValid(stack) ?: false
}
