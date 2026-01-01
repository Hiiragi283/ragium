package hiiragi283.ragium.client.gui.screen

import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.processing.HTMelterBlockEntity
import hiiragi283.ragium.common.inventory.container.HTMelterContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class HTMelterScreen(menu: HTMelterContainerMenu, inventory: Inventory, title: Component) :
    HTProcessorScreen<HTMelterBlockEntity, HTMelterContainerMenu>(menu, inventory, title) {
    override fun init() {
        super.init()
        addFluidTank(HTSlotHelper.getSlotPosX(6.5), blockEntity.outputTank)

        addBurning(HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(1.5))
        addProgress(HTSlotHelper.getSlotPosX(4.5), HTSlotHelper.getSlotPosY(1))
    }
}
