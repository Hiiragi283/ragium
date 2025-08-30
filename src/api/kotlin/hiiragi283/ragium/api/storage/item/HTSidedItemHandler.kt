package hiiragi283.ragium.api.storage.item

import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandlerModifiable

/**
 * 向きに応じて制御された[IItemHandlerModifiable]の拡張インターフェース
 * @see [HTItemHandler]
 * @see [mekanism.api.inventory.ISidedItemHandler]
 */
interface HTSidedItemHandler : IItemHandlerModifiable {
    fun getInventorySideFor(): Direction? = null

    fun setStackInSlot(slot: Int, stack: ItemStack, side: Direction?)

    override fun setStackInSlot(slot: Int, stack: ItemStack) {
        setStackInSlot(slot, stack, getInventorySideFor())
    }

    fun getSlots(side: Direction?): Int

    override fun getSlots(): Int = getSlots(getInventorySideFor())

    fun getStackInSlot(slot: Int, side: Direction?): ItemStack

    override fun getStackInSlot(slot: Int): ItemStack = getStackInSlot(slot, getInventorySideFor())

    fun insertItem(
        slot: Int,
        stack: ItemStack,
        side: Direction?,
        simulate: Boolean,
    ): ItemStack

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack =
        insertItem(slot, stack, getInventorySideFor(), simulate)

    fun extractItem(
        slot: Int,
        amount: Int,
        side: Direction?,
        simulate: Boolean,
    ): ItemStack

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack =
        extractItem(slot, amount, getInventorySideFor(), simulate)

    fun getSlotLimit(slot: Int, side: Direction?): Int

    override fun getSlotLimit(slot: Int): Int = getSlotLimit(slot, getInventorySideFor())

    fun isItemValid(slot: Int, stack: ItemStack, side: Direction?): Boolean

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = isItemValid(slot, stack, getInventorySideFor())
}
