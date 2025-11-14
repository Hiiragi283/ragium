package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.client.gui.component.HTProgressWidget
import hiiragi283.ragium.common.block.entity.device.HTWaterCollectorBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTWaterCollectorScreen(menu: HTBlockEntityContainerMenu<HTWaterCollectorBlockEntity>, inventory: Inventory, title: Component) :
    HTBlockEntityContainerScreen<HTWaterCollectorBlockEntity>(menu, inventory, title) {
    override val texture: ResourceLocation = RagiumAPI.id("textures/gui/container/fluid_collector.png")

    private lateinit var fluidWidget: HTFluidWidget

    override fun init() {
        super.init()
        addRenderableOnly(
            HTProgressWidget.arrow(
                blockEntity::progress,
                startX + HTSlotHelper.getSlotPosX(3.5),
                startY + HTSlotHelper.getSlotPosY(1),
            ),
        )

        fluidWidget = createFluidTank(blockEntity.tank, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(0))
    }

    override fun getFluidWidgets(): List<HTFluidWidget> = listOf(fluidWidget)
}
