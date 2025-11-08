package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.client.gui.component.HTProgressWidget
import hiiragi283.ragium.common.block.entity.consumer.HTConsumerBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
open class HTConsumerScreen<BE : HTConsumerBlockEntity>(
    override val texture: ResourceLocation,
    menu: HTBlockEntityContainerMenu<BE>,
    inventory: Inventory,
    title: Component,
) : HTBlockEntityContainerScreen<BE>(menu, inventory, title) {
    companion object {
        @JvmStatic
        fun <BE : HTConsumerBlockEntity> create(
            texture: ResourceLocation,
        ): MenuScreens.ScreenConstructor<HTBlockEntityContainerMenu<BE>, HTConsumerScreen<BE>> =
            MenuScreens.ScreenConstructor { menu: HTBlockEntityContainerMenu<BE>, inventory: Inventory, title: Component ->
                HTConsumerScreen(texture, menu, inventory, title)
            }
    }

    override fun init() {
        super.init()
        // Progress Widget
        addProgressBar(::addRenderableOnly)
        // Energy Widget
        createEnergyWidget(blockEntity.battery)
    }

    protected open fun addProgressBar(consumer: (HTProgressWidget) -> Unit) {
        consumer(
            HTProgressWidget.arrow(
                blockEntity::progress,
                startX + HTSlotHelper.getSlotPosX(3.5),
                startY + HTSlotHelper.getSlotPosY(1),
            ),
        )
    }
}
