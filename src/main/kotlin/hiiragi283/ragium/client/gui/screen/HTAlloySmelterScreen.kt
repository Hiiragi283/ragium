package hiiragi283.ragium.client.gui.screen

import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.processing.HTAlloySmelterBlockEntity
import hiiragi283.ragium.common.inventory.HTAlloySmelterMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class HTAlloySmelterScreen(menu: HTAlloySmelterMenu, inventory: Inventory, title: Component) :
    HTProcessorScreen.Energized<HTAlloySmelterBlockEntity, HTAlloySmelterMenu>(menu, inventory, title) {
    override fun init() {
        super.init()

        addBurning(HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1))
        addProgress(HTSlotHelper.getSlotPosX(4.5), HTSlotHelper.getSlotPosY(1))
    }
}
