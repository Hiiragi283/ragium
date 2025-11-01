package hiiragi283.ragium.common.storage.proxy

import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.holder.HTCapabilityHolder
import hiiragi283.ragium.api.storage.item.HTSidedItemHandler
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler

/**
 * [IItemHandler]向けの[HTProxyHandler]の実装クラス
 * @param handler ラップ対象の[HTSidedItemHandler]
 * @param side 現在の向き
 * @param holder 搬入出の制御
 * @see mekanism.common.capabilities.proxy.ProxyItemHandler
 */
class HTProxyItemHandler(private val handler: HTSidedItemHandler, side: Direction?, holder: HTCapabilityHolder?) :
    HTProxyHandler(side, holder),
    IItemHandler {
    override fun getSlots(): Int = handler.getSlots(side)

    override fun getStackInSlot(slot: Int): ItemStack = handler.getStackInSlot(slot, side)

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = when (readOnlyInsert) {
        true -> stack
        false -> handler.insertItem(slot, stack, HTStorageAction.of(simulate), side)
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = when (readOnlyExtract) {
        true -> ItemStack.EMPTY
        false -> handler.extractItem(slot, amount, HTStorageAction.of(simulate), side)
    }

    override fun getSlotLimit(slot: Int): Int = handler.getSlotLimit(slot, side)

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean = !readOnly || handler.isItemValid(slot, stack, side)
}
