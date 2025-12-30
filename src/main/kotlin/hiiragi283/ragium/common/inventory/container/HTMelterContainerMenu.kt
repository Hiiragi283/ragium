package hiiragi283.ragium.common.inventory.container

import hiiragi283.core.common.inventory.HTContainerItemSlot
import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.processing.HTMelterBlockEntity
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory

class HTMelterContainerMenu(containerId: Int, inventory: Inventory, context: HTMelterBlockEntity) :
    HTMachineContainerMenu<HTMelterBlockEntity>(RagiumMenuTypes.MELTER, containerId, inventory, context) {
    override fun initSlots() {
        addSlot(HTContainerItemSlot.input(context.inputSlot, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0.5)))

        addUpgradeSlots()
    }
}
