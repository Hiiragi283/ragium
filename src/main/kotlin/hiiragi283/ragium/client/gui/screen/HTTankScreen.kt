package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.common.block.entity.storage.HTTankBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTTankScreen(menu: HTBlockEntityContainerMenu<HTTankBlockEntity>, inventory: Inventory, title: Component) :
    HTBlockEntityContainerScreen<HTTankBlockEntity>(createTexture("tank"), menu, inventory, title) {
    private lateinit var fluidWidget: HTFluidWidget

    override fun init() {
        super.init()
        fluidWidget = createFluidTank(blockEntity.tank, HTSlotHelper.getSlotPosX(4), HTSlotHelper.getSlotPosY(0))
    }

    override fun getFluidWidgets(): List<HTFluidWidget> = listOf(fluidWidget)
}
