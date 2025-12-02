package hiiragi283.ragium.client.gui.screen.processor

import hiiragi283.ragium.client.gui.component.HTFluidTankWidget
import hiiragi283.ragium.client.gui.component.HTProgressWidget
import hiiragi283.ragium.common.block.entity.processor.HTMobCrusherBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class HTMobCrusherScreen(menu: HTBlockEntityContainerMenu<HTMobCrusherBlockEntity>, inventory: Inventory, title: Component) :
    HTSingleFluidProcessorScreen<HTMobCrusherBlockEntity>(menu, inventory, title) {
    override fun createFluidWidget(): HTFluidTankWidget =
        createFluidTank(blockEntity.outputTank, HTSlotHelper.getSlotPosX(6.5), HTSlotHelper.getSlotPosY(0))

    override fun addProgressBar(consumer: (HTProgressWidget) -> Unit) {}
}
