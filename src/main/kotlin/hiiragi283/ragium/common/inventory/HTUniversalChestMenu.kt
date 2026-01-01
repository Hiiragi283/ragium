package hiiragi283.ragium.common.inventory

import hiiragi283.core.api.storage.item.HTItemHandler
import hiiragi283.core.api.storage.item.HTItemSlot
import hiiragi283.core.common.inventory.HTContainerItemSlot
import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.core.common.inventory.container.HTContainerWithContextMenu
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player

class HTUniversalChestMenu(containerId: Int, inventory: Inventory, context: HTItemHandler) :
    HTContainerWithContextMenu<HTItemHandler>(RagiumMenuTypes.UNIVERSAL_CHEST, containerId, inventory, context) {
    init {
        // Universal Chest slots
        val slots: List<HTItemSlot> = context.getItemSlots(null)
        for (i: Int in slots.indices) {
            val slot: HTBasicItemSlot = slots[i] as? HTBasicItemSlot ?: continue
            addSlot(HTContainerItemSlot.both(slot, HTSlotHelper.getSlotPosX(i % 9), HTSlotHelper.getSlotPosY(i / 9)))
        }
        // Player inventory
        addPlayerInv(inventory)
    }

    override fun stillValid(player: Player): Boolean = true
}
