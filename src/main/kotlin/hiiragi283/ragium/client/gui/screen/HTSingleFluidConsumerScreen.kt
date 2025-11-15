package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.client.gui.component.HTFluidTankWidget
import hiiragi283.ragium.common.block.entity.consumer.HTConsumerBlockEntity
import hiiragi283.ragium.common.block.entity.consumer.HTExtractorBlockEntity
import hiiragi283.ragium.common.block.entity.consumer.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.consumer.HTSimulatorBlockEntity
import hiiragi283.ragium.common.block.entity.consumer.base.HTFluidToChancedItemOutputBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTSingleFluidConsumerScreen<BE : HTConsumerBlockEntity>(
    private val factory: HTSingleFluidConsumerScreen<BE>.() -> HTFluidTankWidget,
    texture: ResourceLocation,
    menu: HTBlockEntityContainerMenu<BE>,
    inventory: Inventory,
    title: Component,
) : HTConsumerScreen<BE>(texture, menu, inventory, title) {
    companion object {
        @JvmStatic
        fun chancedItemOutput(
            menu: HTBlockEntityContainerMenu<HTFluidToChancedItemOutputBlockEntity<*, *>>,
            inventory: Inventory,
            title: Component,
        ): HTSingleFluidConsumerScreen<HTFluidToChancedItemOutputBlockEntity<*, *>> = HTSingleFluidConsumerScreen(
            { createFluidSlot(blockEntity.inputTank, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)) },
            RagiumAPI.id("textures/gui/container/crusher.png"),
            menu,
            inventory,
            title,
        )

        @JvmStatic
        fun extractor(
            menu: HTBlockEntityContainerMenu<HTExtractorBlockEntity>,
            inventory: Inventory,
            title: Component,
        ): HTSingleFluidConsumerScreen<HTExtractorBlockEntity> = HTSingleFluidConsumerScreen(
            { createFluidSlot(blockEntity.outputTank, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(2)) },
            RagiumAPI.id("textures/gui/container/extractor.png"),
            menu,
            inventory,
            title,
        )

        @JvmStatic
        fun melter(
            menu: HTBlockEntityContainerMenu<HTMelterBlockEntity>,
            inventory: Inventory,
            title: Component,
        ): HTSingleFluidConsumerScreen<HTMelterBlockEntity> = HTSingleFluidConsumerScreen(
            { createFluidTank(blockEntity.outputTank, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(0)) },
            RagiumAPI.id("textures/gui/container/melter.png"),
            menu,
            inventory,
            title,
        )

        @JvmStatic
        fun simulator(
            menu: HTBlockEntityContainerMenu<HTSimulatorBlockEntity>,
            inventory: Inventory,
            title: Component,
        ): HTSingleFluidConsumerScreen<HTSimulatorBlockEntity> = HTSingleFluidConsumerScreen(
            { createFluidSlot(blockEntity.outputTank, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(2)) },
            RagiumAPI.id("textures/gui/container/simulator.png"),
            menu,
            inventory,
            title,
        )
    }

    private lateinit var fluidWidget: HTFluidWidget

    override fun init() {
        super.init()
        fluidWidget = this.factory()
    }

    override fun getFluidWidgets(): List<HTFluidWidget> = listOf(fluidWidget)
}
