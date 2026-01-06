package hiiragi283.ragium.common.inventory

import hiiragi283.core.common.inventory.HTContainerItemSlot
import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.processing.HTAlloySmelterBlockEntity
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory

class HTAlloySmelterMenu(containerId: Int, inventory: Inventory, context: HTAlloySmelterBlockEntity) :
    HTUpgradableContainerMenu<HTAlloySmelterBlockEntity>(RagiumMenuTypes.ALLOY_SMELTER, containerId, inventory, context) {
    override fun initSlots() {
        addSlot(HTContainerItemSlot.input(context.topInputSlot, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(0)))
        addSlot(HTContainerItemSlot.input(context.leftInputSlot, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)))
        addSlot(HTContainerItemSlot.input(context.rightInputSlot, HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(2)))

        addSlot(HTContainerItemSlot.output(context.outputSlot, HTSlotHelper.getSlotPosX(6.5), HTSlotHelper.getSlotPosY(1)))

        addUpgradeSlots()
    }
}
