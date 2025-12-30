package hiiragi283.ragium.common.inventory.container

import hiiragi283.core.common.inventory.HTContainerItemSlot
import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.core.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.ragium.common.block.entity.processing.HTAbstractComplexBlockEntity
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.world.entity.player.Inventory

class HTComplexContainerMenu(containerId: Int, inventory: Inventory, context: HTAbstractComplexBlockEntity<*>) :
    HTBlockEntityContainerMenu<HTAbstractComplexBlockEntity<*>>(RagiumMenuTypes.COMPLEX, containerId, inventory, context) {
    override fun initSlots() {
        addSlot(HTContainerItemSlot.input(context.inputSlot, HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(0.5)))

        addSlot(HTContainerItemSlot.output(context.outputSlot, HTSlotHelper.getSlotPosX(6.5), HTSlotHelper.getSlotPosY(1)))
    }
}
