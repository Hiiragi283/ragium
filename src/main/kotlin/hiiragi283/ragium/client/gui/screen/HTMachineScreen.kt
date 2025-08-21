package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.gui.component.HTEnergyNetworkWidget
import hiiragi283.ragium.api.gui.component.HTProgressWidget
import hiiragi283.ragium.api.gui.screen.HTContainerScreen
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
open class HTMachineScreen<BE : HTMachineBlockEntity>(
    override val texture: ResourceLocation,
    menu: HTBlockEntityContainerMenu<BE>,
    inventory: Inventory,
    title: Component,
) : HTContainerScreen<HTBlockEntityContainerMenu<BE>>(menu, inventory, title) {
    companion object {
        @JvmStatic
        fun <BE : HTMachineBlockEntity> create(
            texture: ResourceLocation,
        ): MenuScreens.ScreenConstructor<HTBlockEntityContainerMenu<BE>, HTMachineScreen<BE>> =
            MenuScreens.ScreenConstructor { menu: HTBlockEntityContainerMenu<BE>, inventory: Inventory, title: Component ->
                HTMachineScreen(texture, menu, inventory, title)
            }
    }

    protected lateinit var energyWidget: HTEnergyNetworkWidget
        private set

    override fun init() {
        super.init()
        // Progress Widget
        addProgressBar(::addRenderableOnly)
        // Energy Widget
        energyWidget = addRenderableWidget(createEnergyWidget(menu.context.getDimension()))
    }

    protected open fun addProgressBar(consumer: (HTProgressWidget) -> Unit) {
        consumer(
            HTProgressWidget.arrow(
                menu.context::progress,
                startX + HTSlotHelper.getSlotPosX(3.5),
                startY + HTSlotHelper.getSlotPosY(1),
            ),
        )
    }
}
