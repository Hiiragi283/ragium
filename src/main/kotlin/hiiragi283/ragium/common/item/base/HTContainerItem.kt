package hiiragi283.ragium.common.item.base

import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

abstract class HTContainerItem(properties: Properties) : Item(properties) {
    companion object {
        @JvmStatic
        fun getHandler(stack: ItemStack): HTItemHandler? = RagiumCapabilities.ITEM.getSlottedCapability(stack)

        @JvmStatic
        fun getItemSlot(stack: ItemStack, slot: Int): HTItemSlot? = RagiumCapabilities.ITEM.getCapabilitySlot(stack, slot)

        @JvmStatic
        fun getItemSlots(stack: ItemStack): List<HTItemSlot> = RagiumCapabilities.ITEM.getCapabilitySlots(stack)

        @JvmStatic
        fun hasHandler(stack: ItemStack): Boolean = getItemSlots(stack).isEmpty()
    }
}
