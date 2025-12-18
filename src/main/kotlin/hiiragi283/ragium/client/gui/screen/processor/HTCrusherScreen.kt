package hiiragi283.ragium.client.gui.screen.processor

import hiiragi283.ragium.client.gui.component.base.HTBasicFluidWidget
import hiiragi283.ragium.common.block.entity.processor.base.HTAbstractCrusherBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.ragium.common.upgrade.RagiumUpgradeKeys
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class HTCrusherScreen(menu: HTBlockEntityContainerMenu<HTAbstractCrusherBlockEntity>, inventory: Inventory, title: Component) :
    HTSingleFluidProcessorScreen<HTAbstractCrusherBlockEntity>(
        menu,
        inventory,
        title,
    ) {
    override fun createFluidWidget(): HTBasicFluidWidget =
        createFluidSlot(blockEntity.inputTank, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2))

    override fun updateVisibility() {
        fluidWidget.visible = blockEntity.hasUpgrade(RagiumUpgradeKeys.USE_LUBRICANT)
    }
}
