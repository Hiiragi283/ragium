package hiiragi283.ragium.common.inventory

import hiiragi283.core.common.inventory.HTContainerItemSlot
import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.processing.HTMelterBlockEntity
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory

class HTMelterMenu(containerId: Int, inventory: Inventory, context: HTMelterBlockEntity) :
    HTUpgradableContainerMenu<HTMelterBlockEntity>(RagiumMenuTypes.MELTER, containerId, inventory, context) {
    override fun initSlots() {
        addSlot(HTContainerItemSlot.input(context.inputSlot, HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(0.5)))

        addUpgradeSlots()
    }
}
