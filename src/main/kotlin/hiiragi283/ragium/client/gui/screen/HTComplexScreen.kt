package hiiragi283.ragium.client.gui.screen

import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.processing.HTComplexBlockEntity
import hiiragi283.ragium.common.inventory.HTComplexMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class HTComplexScreen<BE : HTComplexBlockEntity>(menu: HTComplexMenu<BE>, inventory: Inventory, title: Component) :
    HTProcessorScreen<BE, HTComplexMenu<BE>>(menu, inventory, title) {
    override fun init() {
        super.init()
        addFluidTank(HTSlotHelper.getSlotPosX(1.5), blockEntity.inputTank)
        addFluidTank(HTSlotHelper.getSlotPosX(8), blockEntity.outputTank)

        addBurning(HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(1.5))
        addProgress(HTSlotHelper.getSlotPosX(4.5), HTSlotHelper.getSlotPosY(1))
    }
}
