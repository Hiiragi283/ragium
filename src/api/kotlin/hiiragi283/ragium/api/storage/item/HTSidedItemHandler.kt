package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.storage.HTStorageAction
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler

/**
 * 向きに応じて制御された[IItemHandler]の拡張インターフェース
 * @see mekanism.api.inventory.ISidedItemHandler
 */
interface HTSidedItemHandler : IItemHandler {
    fun getItemSideFor(): Direction? = null

    fun getSlots(side: Direction?): Int

    @Deprecated("Use `getSlots(Direction?)` instead")
    override fun getSlots(): Int = getSlots(getItemSideFor())

    fun getStackInSlot(slot: Int, side: Direction?): ItemStack

    @Deprecated("Use `getStackInSlot(Int, Direction?)` instead")
    override fun getStackInSlot(slot: Int): ItemStack = getStackInSlot(slot, getItemSideFor())

    fun insertItem(
        slot: Int,
        stack: ItemStack,
        action: HTStorageAction,
        side: Direction?,
    ): ItemStack

    @Deprecated("Use `insertItem(Int, ItemStack, Boolean, Direction?)` instead")
    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack =
        insertItem(slot, stack, HTStorageAction.of(simulate), getItemSideFor())

    fun extractItem(
        slot: Int,
        amount: Int,
        action: HTStorageAction,
        side: Direction?,
    ): ItemStack

    @Deprecated("Use `insertItem(Int, Int, Boolean, Direction?)` instead")
    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack =
        extractItem(slot, amount, HTStorageAction.of(simulate), getItemSideFor())

    fun getSlotLimit(slot: Int, side: Direction?): Int

    @Deprecated("Use `getSlotLimit(Int, Direction?)` instead")
    override fun getSlotLimit(slot: Int): Int = getSlotLimit(slot, getItemSideFor())

    fun isItemValid(slot: Int, stack: ItemStack, side: Direction?): Boolean

    @Deprecated("Use `isItemValid(Int, ItemStack, Direction?)` instead")
    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = isItemValid(slot, stack, getItemSideFor())
}
