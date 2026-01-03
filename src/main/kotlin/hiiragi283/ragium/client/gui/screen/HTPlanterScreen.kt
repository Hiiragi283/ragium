package hiiragi283.ragium.client.gui.screen

import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.processing.HTPlanterBlockEntity
import hiiragi283.ragium.common.inventory.HTPlanterMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class HTPlanterScreen(menu: HTPlanterMenu, inventory: Inventory, title: Component) :
    HTProcessorScreen<HTPlanterBlockEntity, HTPlanterMenu>(menu, inventory, title) {
    override fun init() {
        super.init()
        addFluidTank(HTSlotHelper.getSlotPosX(1.5), blockEntity.inputTank)

        addProgress(HTSlotHelper.getSlotPosX(4.5), HTSlotHelper.getSlotPosY(1))
    }
}
