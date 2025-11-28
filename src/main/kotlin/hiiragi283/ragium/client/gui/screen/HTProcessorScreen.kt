package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.client.gui.component.HTProgressWidget
import hiiragi283.ragium.common.block.entity.processor.HTEnergizedProcessorBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
open class HTProcessorScreen<BE : HTEnergizedProcessorBlockEntity<*, *>> : HTBlockEntityContainerScreen<BE> {
    companion object {
        @JvmStatic
        fun <BE : HTEnergizedProcessorBlockEntity<*, *>> createFactory(texture: String): HTBlockEntityScreenFactory<BE> =
            createFactory(RagiumAPI.id("textures", "gui", "container", "$texture.png"))

        @JvmStatic
        fun <BE : HTEnergizedProcessorBlockEntity<*, *>> createFactory(texture: ResourceLocation): HTBlockEntityScreenFactory<BE> =
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
