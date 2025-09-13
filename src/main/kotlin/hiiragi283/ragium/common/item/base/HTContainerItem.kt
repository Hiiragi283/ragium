package hiiragi283.ragium.common.item.base

import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

abstract class HTContainerItem(properties: Properties) : Item(properties) {
    companion object {
        @JvmStatic
        fun getHandler(stack: ItemStack): HTItemHandler? = HTMultiCapability.ITEM.getCapability(stack) as? HTItemHandler

        @JvmStatic
        fun getItemSlot(stack: ItemStack, slot: Int): HTItemSlot? {
            val handler: HTItemHandler = getHandler(stack) ?: return null
            return handler.getItemSlot(slot, handler.getItemSideFor())
        }

        @JvmStatic
        fun getItemSlots(stack: ItemStack): List<HTItemSlot> {
            val handler: HTItemHandler = getHandler(stack) ?: return listOf()
            return handler.getItemSlots(handler.getItemSideFor())
        }

        @JvmStatic
        fun hasHandler(stack: ItemStack): Boolean = getItemSlots(stack).isEmpty()
    }
}
