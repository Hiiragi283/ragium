package hiiragi283.ragium.common.inventory

import hiiragi283.core.common.inventory.HTContainerItemSlot
import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.storage.HTTankBlockEntity
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory

class HTTankMenu(containerId: Int, inventory: Inventory, context: HTTankBlockEntity) :
    HTUpgradableContainerMenu<HTTankBlockEntity>(RagiumMenuTypes.TANK, containerId, inventory, context) {
    override fun initSlots() {
        addSlot(HTContainerItemSlot.input(context.emptySlot, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0)))
        addSlot(HTContainerItemSlot.input(context.fillSlot, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)))

        addSlot(HTContainerItemSlot.input(context.outputSlot, HTSlotHelper.getSlotPosX(4), HTSlotHelper.getSlotPosY(1)))

        addUpgradeSlots()
    }
}
