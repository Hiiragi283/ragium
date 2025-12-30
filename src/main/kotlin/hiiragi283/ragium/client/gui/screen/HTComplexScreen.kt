package hiiragi283.ragium.client.gui.screen

import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.processing.HTAbstractComplexBlockEntity
import hiiragi283.ragium.common.inventory.container.HTComplexContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class HTComplexScreen(menu: HTComplexContainerMenu, inventory: Inventory, title: Component) :
    HTProcessorScreen<HTAbstractComplexBlockEntity<*>, HTComplexContainerMenu>(menu, inventory, title) {
    override fun init() {
        super.init()
        addFluidTank(HTSlotHelper.getSlotPosX(1.5), blockEntity.inputTank)
        addFluidTank(HTSlotHelper.getSlotPosX(8), blockEntity.outputTank)

        addBurning(HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(1.5))
        addProgress(HTSlotHelper.getSlotPosX(4.5), HTSlotHelper.getSlotPosY(1))
    }
}
