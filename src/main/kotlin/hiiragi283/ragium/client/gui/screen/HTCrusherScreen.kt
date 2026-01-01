package hiiragi283.ragium.client.gui.screen

import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.processing.HTCrusherBlockEntity
import hiiragi283.ragium.common.inventory.HTMultiItemOutputMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class HTCrusherScreen(menu: HTMultiItemOutputMenu<HTCrusherBlockEntity>, inventory: Inventory, title: Component) :
    HTProcessorScreen<HTCrusherBlockEntity, HTMultiItemOutputMenu<HTCrusherBlockEntity>>(menu, inventory, title) {
    override fun init() {
        super.init()
        addFluidTank(HTSlotHelper.getSlotPosX(1.5), blockEntity.inputTank)

        // addBurning(HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(1.5))
        addProgress(HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(1))
    }
}
