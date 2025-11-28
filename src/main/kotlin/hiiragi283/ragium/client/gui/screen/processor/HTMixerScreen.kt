package hiiragi283.ragium.client.gui.screen.processor

import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.processor.HTMixerBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTMixerScreen(menu: HTBlockEntityContainerMenu<HTMixerBlockEntity>, inventory: Inventory, title: Component) :
    HTProcessorScreen<HTMixerBlockEntity>(menu, inventory, title) {
    private lateinit var fluidWidget: HTFluidWidget
    private lateinit var fluidWidget1: HTFluidWidget

    override fun init() {
        super.init()
        fluidWidget = createFluidSlot(blockEntity.inputTank, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2))
        fluidWidget1 = createFluidSlot(blockEntity.outputTank, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(2))
    }

    override fun getFluidWidgets(): List<HTFluidWidget> = listOf(fluidWidget, fluidWidget1)
}
