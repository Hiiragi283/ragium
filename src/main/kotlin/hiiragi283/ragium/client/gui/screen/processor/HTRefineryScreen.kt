package hiiragi283.ragium.client.gui.screen.processor

import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.common.block.entity.processor.HTRefineryBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTRefineryScreen(menu: HTBlockEntityContainerMenu<HTRefineryBlockEntity>, inventory: Inventory, title: Component) :
    HTProcessorScreen<HTRefineryBlockEntity>(menu, inventory, title) {
    private lateinit var fluidWidget: HTFluidWidget
    private lateinit var fluidWidget1: HTFluidWidget

    override fun init() {
        super.init()
        fluidWidget = createFluidTank(blockEntity.inputTank, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        fluidWidget1 = createFluidTank(blockEntity.outputTank, HTSlotHelper.getSlotPosX(6), HTSlotHelper.getSlotPosY(0))
    }

    override fun getFluidWidgets(): List<HTFluidWidget> = listOf(fluidWidget, fluidWidget1)
}
