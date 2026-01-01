package hiiragi283.ragium.common.inventory

import hiiragi283.core.common.inventory.HTContainerItemSlot
import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.processing.HTCuttingMachineBlockEntity
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory

class HTCuttingMachineMenu(containerId: Int, inventory: Inventory, context: HTCuttingMachineBlockEntity) :
    HTMachineContainerMenu<HTCuttingMachineBlockEntity>(RagiumMenuTypes.CUTTING_MACHINE, containerId, inventory, context) {
    override fun initSlots() {
        addSlot(HTContainerItemSlot.input(context.inputSlot, HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(0.5)))

        addSlot(HTContainerItemSlot.output(context.outputSlots[0], HTSlotHelper.getSlotPosX(6.5), HTSlotHelper.getSlotPosY(0.5)))
        addSlot(HTContainerItemSlot.output(context.outputSlots[1], HTSlotHelper.getSlotPosX(6.5), HTSlotHelper.getSlotPosY(2)))

        addUpgradeSlots()
    }
}
