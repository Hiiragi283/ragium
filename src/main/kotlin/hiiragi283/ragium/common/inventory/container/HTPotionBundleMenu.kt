package hiiragi283.ragium.common.inventory.container

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.inventory.container.HTContainerWithContextMenu
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory
import net.neoforged.neoforge.items.IItemHandler

class HTPotionBundleMenu(containerId: Int, inventory: Inventory, context: IItemHandler) :
    HTContainerWithContextMenu<IItemHandler>(RagiumMenuTypes.POTION_BUNDLE, containerId, inventory, context) {
    init {
        // potion slot
        for (i: Int in (0 until context.slots)) {
            addInputSlot(context, i, HTSlotHelper.getSlotPosX(i), HTSlotHelper.getSlotPosY(0) + 2)
        }
        // player inventory
        addPlayerInv(inventory, -HTSlotHelper.getSlotPosY(1) + 3)
    }
}
