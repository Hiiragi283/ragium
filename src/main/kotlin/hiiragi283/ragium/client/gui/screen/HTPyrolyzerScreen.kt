package hiiragi283.ragium.client.gui.screen

import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.processing.HTPyrolyzerBlockEntity
import hiiragi283.ragium.common.inventory.HTMultiItemOutputMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class HTPyrolyzerScreen(menu: HTMultiItemOutputMenu<HTPyrolyzerBlockEntity>, inventory: Inventory, title: Component) :
    HTProcessorScreen<HTPyrolyzerBlockEntity, HTMultiItemOutputMenu<HTPyrolyzerBlockEntity>>(menu, inventory, title) {
    override fun init() {
        super.init()
        addFluidTank(HTSlotHelper.getSlotPosX(8), blockEntity.outputTank)

        addBurning(HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(1.5))
        addProgress(HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(1))
    }
}
