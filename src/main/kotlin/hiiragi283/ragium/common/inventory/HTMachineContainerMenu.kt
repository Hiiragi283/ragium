package hiiragi283.ragium.common.inventory

import hiiragi283.core.common.inventory.HTContainerItemSlot
import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.core.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.core.common.registry.HTDeferredMenuType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import net.minecraft.world.entity.player.Inventory

abstract class HTMachineContainerMenu<BE : HTMachineBlockEntity>(
    menuType: HTDeferredMenuType.WithContext<*, BE>,
    containerId: Int,
    inventory: Inventory,
    context: BE,
) : HTBlockEntityContainerMenu<BE>(menuType, containerId, inventory, context) {
    protected fun addUpgradeSlots() {
        context.machineUpgrade
            .getUpgradeSlots()
            .forEachIndexed { index: Int, slot: HTBasicItemSlot ->
                addSlot(HTContainerItemSlot.both(slot, HTSlotHelper.getSlotPosX(9.5), HTSlotHelper.getSlotPosY(0.5 + index)))
            }
    }
}
