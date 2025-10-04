package hiiragi283.ragium.api.storage.item

import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler

/**
 * 向きに応じて制御された[IItemHandler]の拡張インターフェース
 * @see [HTItemHandler]
 * @see [mekanism.api.inventory.ISidedItemHandler]
 */
interface HTSidedItemHandler : IItemHandler {
    fun getItemSideFor(): Direction? = null

    fun getSlots(side: Direction?): Int

    override fun getSlots(): Int = getSlots(getItemSideFor())

    fun getStackInSlot(slot: Int, side: Direction?): ItemStack

    override fun getStackInSlot(slot: Int): ItemStack = getStackInSlot(slot, getItemSideFor())

    fun insertItem(
        slot: Int,
        stack: ItemStack,
        side: Direction?,
        simulate: Boolean,
    ): ItemStack

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = insertItem(slot, stack, getItemSideFor(), simulate)

    fun extractItem(
        slot: Int,
        amount: Int,
        side: Direction?,
        simulate: Boolean,
    ): ItemStack

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = extractItem(slot, amount, getItemSideFor(), simulate)

    fun getSlotLimit(slot: Int, side: Direction?): Int

    override fun getSlotLimit(slot: Int): Int = getSlotLimit(slot, getItemSideFor())

    fun isItemValid(slot: Int, stack: ItemStack, side: Direction?): Boolean

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = isItemValid(slot, stack, getItemSideFor())
}
