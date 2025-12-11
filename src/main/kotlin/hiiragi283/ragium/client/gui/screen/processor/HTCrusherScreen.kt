package hiiragi283.ragium.client.gui.screen.processor

import hiiragi283.ragium.client.gui.component.HTFluidTankWidget
import hiiragi283.ragium.common.block.entity.processor.base.HTSingleItemInputBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class HTCrusherScreen(
    menu: HTBlockEntityContainerMenu<HTSingleItemInputBlockEntity.CachedWithTank<*>>,
    inventory: Inventory,
    title: Component,
) : HTSingleFluidProcessorScreen<HTSingleItemInputBlockEntity.CachedWithTank<*>>(
        menu,
        inventory,
        title,
    ) {
    override fun createFluidWidget(): HTFluidTankWidget =
        createFluidSlot(blockEntity.inputTank, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2))

    override fun updateVisibility() {
        fluidWidget.visible = blockEntity.machineUpgrade.hasUpgrade(RagiumItems.EFFICIENT_CRUSH_UPGRADE)
    }
}
