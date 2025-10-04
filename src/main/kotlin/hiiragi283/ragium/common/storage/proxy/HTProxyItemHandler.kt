package hiiragi283.ragium.common.storage.proxy

import hiiragi283.ragium.api.storage.holder.HTCapabilityHolder
import hiiragi283.ragium.api.storage.item.HTSidedItemHandler
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandlerModifiable

/**
 * [IItemHandlerModifiable]向けの[HTProxyHandler]の実装クラス
 * @param inventory ラップ対象の[HTSidedItemHandler]
 * @param side 現在の向き
 * @param holder 搬入出の制御
 * @see [mekanism.common.capabilities.proxy.ProxyItemHandler]
 */
class HTProxyItemHandler(private val inventory: HTSidedItemHandler, side: Direction?, holder: HTCapabilityHolder?) :
    HTProxyHandler(side, holder),
    IItemHandlerModifiable {
    override fun setStackInSlot(slot: Int, stack: ItemStack) {
        if (!readOnly) {
            inventory.setStackInSlot(slot, stack, side)
        }
    }

    override fun getSlots(): Int = inventory.getSlots(side)

    override fun getStackInSlot(slot: Int): ItemStack = inventory.getStackInSlot(slot, side)

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = when (readOnlyInsert) {
        true -> stack
        false -> inventory.insertItem(slot, stack, side, simulate)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = when (readOnlyExtract) {
        true -> ItemStack.EMPTY
        false -> inventory.extractItem(slot, amount, side, simulate)
    }

    override fun getSlotLimit(slot: Int): Int = inventory.getSlotLimit(slot, side)

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = !readOnly || inventory.isItemValid(slot, stack, side)
}
