package hiiragi283.ragium.client.gui.screen

import hiiragi283.core.common.inventory.HTSlotHelper
import hiiragi283.core.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.ragium.common.block.entity.processing.HTChancedBlockEntity
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class HTChancedScreen<BE : HTChancedBlockEntity, MENU : HTBlockEntityContainerMenu<BE>>(
    menu: MENU,
    inventory: Inventory,
    title: Component,
) : HTProcessorScreen<BE, MENU>(menu, inventory, title) {
    override fun init() {
        super.init()
        addFluidTank(HTSlotHelper.getSlotPosX(1.5), blockEntity.inputTank)

        // addBurning(HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(1.5))
        addProgress(HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(1))
    }
}
