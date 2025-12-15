package hiiragi283.ragium.client.gui.screen.processor

import hiiragi283.ragium.client.gui.component.base.HTBasicFluidWidget
import hiiragi283.ragium.common.block.entity.processor.HTExtractorBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.processor.base.HTAbstractCombinerBlockEntity
import hiiragi283.ragium.common.inventory.HTSlotHelper
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
abstract class HTSingleFluidProcessorScreen<BE : HTProcessorBlockEntity<*, *>> : HTProcessorScreen<BE> {
    constructor(
        texture: ResourceLocation,
        menu: HTBlockEntityContainerMenu<BE>,
        inventory: Inventory,
        title: Component,
    ) : super(texture, menu, inventory, title)

    constructor(
        menu: HTBlockEntityContainerMenu<BE>,
        inventory: Inventory,
        title: Component,
    ) : super(menu, inventory, title)

    companion object {
        @JvmStatic
        fun <BE : HTAbstractCombinerBlockEntity> combine(
            menu: HTBlockEntityContainerMenu<BE>,
            inventory: Inventory,
            title: Component,
        ): HTSingleFluidProcessorScreen<BE> = Impl(
            { createFluidSlot(blockEntity.inputTank, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)) },
            menu,
            inventory,
            title,
        )

        @JvmStatic
        fun extractor(
            menu: HTBlockEntityContainerMenu<HTExtractorBlockEntity>,
            inventory: Inventory,
            title: Component,
        ): HTSingleFluidProcessorScreen<HTExtractorBlockEntity> = Impl(
            { createFluidSlot(blockEntity.outputTank, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(2)) },
            menu,
            inventory,
            title,
        )

        @JvmStatic
        fun melter(
            menu: HTBlockEntityContainerMenu<HTMelterBlockEntity>,
            inventory: Inventory,
            title: Component,
        ): HTSingleFluidProcessorScreen<HTMelterBlockEntity> = Impl(
            { createFluidTank(blockEntity.outputTank, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(0)) },
            menu,
            inventory,
            title,
        )
    }

    lateinit var fluidWidget: HTBasicFluidWidget
        private set

    override fun init() {
        super.init()
        fluidWidget = createFluidWidget()
    }

    protected abstract fun createFluidWidget(): HTBasicFluidWidget

    private class Impl<BE : HTProcessorBlockEntity<*, *>>(
        private val factory: HTSingleFluidProcessorScreen<BE>.() -> HTBasicFluidWidget,
        menu: HTBlockEntityContainerMenu<BE>,
        inventory: Inventory,
        title: Component,
    ) : HTSingleFluidProcessorScreen<BE>(menu, inventory, title) {
        override fun createFluidWidget(): HTBasicFluidWidget = factory()
    }
}
