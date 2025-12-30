package hiiragi283.ragium.client.gui.screen

import hiiragi283.core.api.function.partially1
import hiiragi283.core.client.gui.component.HTProgressWidget
import hiiragi283.core.client.gui.screen.HTBlockEntityContainerScreen
import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.ragium.client.gui.component.HTFluidTankWidget
import hiiragi283.ragium.common.block.entity.processing.HTMelterBlockEntity
import hiiragi283.ragium.common.inventory.container.HTMelterContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class HTMelterScreen(menu: HTMelterContainerMenu, inventory: Inventory, title: Component) :
    HTBlockEntityContainerScreen<HTMelterBlockEntity, HTMelterContainerMenu>(menu, inventory, title) {
    override fun init() {
        super.init()
        addFluidWidget(HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(0), ::HTFluidTankWidget.partially1(blockEntity.outputTank))

        addWidget(
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(1.5),
            HTProgressWidget::burn.partially1(blockEntity::getReversedProgress),
        )
        addWidget(
            HTSlotHelper.getSlotPosX(4),
            HTSlotHelper.getSlotPosY(1),
            HTProgressWidget::arrow.partially1(blockEntity::getProgress),
        )
    }
}
