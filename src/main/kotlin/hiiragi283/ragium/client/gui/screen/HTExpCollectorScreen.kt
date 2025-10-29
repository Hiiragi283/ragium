package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTExperienceWidget
import hiiragi283.ragium.api.gui.screen.HTExperienceScreen
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.client.gui.component.HTProgressWidget
import hiiragi283.ragium.common.block.entity.device.HTExpCollectorBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTExpCollectorScreen(menu: HTBlockEntityContainerMenu<HTExpCollectorBlockEntity>, inventory: Inventory, title: Component) :
    HTBlockEntityContainerScreen<HTExpCollectorBlockEntity>(menu, inventory, title),
    HTExperienceScreen {
    override val texture: ResourceLocation = RagiumAPI.id("textures/gui/container/fluid_collector.png")

    private lateinit var expWidget: HTExperienceWidget

    override fun init() {
        super.init()
        addRenderableOnly(
            HTProgressWidget.arrow(
                blockEntity::progress,
                startX + HTSlotHelper.getSlotPosX(3.5),
                startY + HTSlotHelper.getSlotPosY(1),
            ),
        )

        expWidget = createExperienceTank(HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(0))
    }

    //    HTExperienceScreen    //

    override fun getExperienceWidget(): HTExperienceWidget = expWidget
}
