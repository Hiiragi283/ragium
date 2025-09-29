package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.client.gui.component.HTProgressWidget
import hiiragi283.ragium.common.block.entity.machine.HTCompressorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTExtractorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.machine.HTPulverizerBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTItemToItemScreen<BE : HTProcessorBlockEntity<SingleRecipeInput, *>>(
    texture: ResourceLocation,
    private val factory: (() -> Float, Int, Int) -> HTProgressWidget,
    menu: HTBlockEntityContainerMenu<BE>,
    inventory: Inventory,
    title: Component,
) : HTMachineScreen<BE>(texture, menu, inventory, title) {
    companion object {
        @JvmStatic
        fun compressor(
            menu: HTBlockEntityContainerMenu<HTCompressorBlockEntity>,
            inventory: Inventory,
            title: Component,
        ): HTItemToItemScreen<HTCompressorBlockEntity> = HTItemToItemScreen(
            RagiumAPI.id("textures/gui/container/compressor.png"),
            HTProgressWidget::infuse,
            menu,
            inventory,
            title,
        )

        @JvmStatic
        fun extractor(
            menu: HTBlockEntityContainerMenu<HTExtractorBlockEntity>,
            inventory: Inventory,
            title: Component,
        ): HTItemToItemScreen<HTExtractorBlockEntity> = HTItemToItemScreen(
            RagiumAPI.id("textures/gui/container/extractor.png"),
            HTProgressWidget::arrow,
            menu,
            inventory,
            title,
        )

        @JvmStatic
        fun pulverizer(
            menu: HTBlockEntityContainerMenu<HTPulverizerBlockEntity>,
            inventory: Inventory,
            title: Component,
        ): HTItemToItemScreen<HTPulverizerBlockEntity> = HTItemToItemScreen(
            RagiumAPI.id("textures/gui/container/pulverizer.png"),
            HTProgressWidget::arrow,
            menu,
            inventory,
            title,
        )
    }

    override fun addProgressBar(consumer: (HTProgressWidget) -> Unit) {
        consumer(factory(blockEntity::progress, startX + HTSlotHelper.getSlotPosX(3.5), startY + HTSlotHelper.getSlotPosY(1)))
    }
}
