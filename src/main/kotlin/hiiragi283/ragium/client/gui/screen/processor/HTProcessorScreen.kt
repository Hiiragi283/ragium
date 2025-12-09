package hiiragi283.ragium.client.gui.screen.processor

import hiiragi283.ragium.client.gui.component.HTProgressWidget
import hiiragi283.ragium.client.gui.screen.HTBlockEntityContainerScreen
import hiiragi283.ragium.client.gui.screen.HTBlockEntityScreenFactory
import hiiragi283.ragium.common.block.entity.processor.HTProcessorBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
open class HTProcessorScreen<BE : HTProcessorBlockEntity<*, *>> : HTBlockEntityContainerScreen<BE> {
    companion object {
        @JvmStatic
        fun <BE : HTProcessorBlockEntity<*, *>> createFactory(texture: String): HTBlockEntityScreenFactory<BE> =
            createFactory(createTexture(texture))

        @JvmStatic
        fun <BE : HTProcessorBlockEntity<*, *>> createFactory(texture: ResourceLocation): HTBlockEntityScreenFactory<BE> =
            HTBlockEntityScreenFactory { menu: HTBlockEntityContainerMenu<BE>, inventory: Inventory, title: Component ->
                HTProcessorScreen(texture, menu, inventory, title)
            }
    }

    constructor(
        texture: ResourceLocation,
        menu: HTBlockEntityContainerMenu<BE>,
        inventory: Inventory,
        title: Component,
    ) : super(texture, menu, inventory, title)

    constructor(menu: HTBlockEntityContainerMenu<BE>, inventory: Inventory, title: Component) : super(
        menu,
        inventory,
        title,
    )

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
                blockEntity::getProgress,
                startX + HTSlotHelper.getSlotPosX(3.5),
                startY + HTSlotHelper.getSlotPosY(1),
            ),
        )
    }
}
