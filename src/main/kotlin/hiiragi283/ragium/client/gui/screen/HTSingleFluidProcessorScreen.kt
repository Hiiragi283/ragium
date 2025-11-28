package hiiragi283.ragium.client.gui.screen

import hiiragi283.ragium.api.gui.component.HTFluidWidget
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.client.gui.component.HTFluidTankWidget
import hiiragi283.ragium.common.block.entity.processor.HTEnchantCopierBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTEnergizedProcessorBlockEntity
import hiiragi283.ragium.common.block.entity.processor.HTMelterBlockEntity
import hiiragi283.ragium.common.block.entity.processor.base.HTFluidToChancedItemOutputBlockEntity
import hiiragi283.ragium.common.block.entity.processor.base.HTItemWithCatalystBlockEntity
import hiiragi283.ragium.common.block.entity.processor.base.HTSingleItemInputBlockEntity
import hiiragi283.ragium.common.inventory.container.HTBlockEntityContainerMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTSingleFluidProcessorScreen<BE : HTEnergizedProcessorBlockEntity<*, *>> : HTProcessorScreen<BE> {
    private val factory: HTSingleFluidProcessorScreen<BE>.() -> HTFluidTankWidget

    constructor(
        factory: HTSingleFluidProcessorScreen<BE>.() -> HTFluidTankWidget,
        texture: ResourceLocation,
        menu: HTBlockEntityContainerMenu<BE>,
        inventory: Inventory,
        title: Component,
    ) : super(texture, menu, inventory, title) {
        this.factory = factory
    }

    constructor(
        factory: HTSingleFluidProcessorScreen<BE>.() -> HTFluidTankWidget,
        menu: HTBlockEntityContainerMenu<BE>,
        inventory: Inventory,
        title: Component,
    ) : super(menu, inventory, title) {
        this.factory = factory
    }

    companion object {
        @JvmStatic
        fun <BE : HTFluidToChancedItemOutputBlockEntity<*, *>> chancedItemOutput(
            menu: HTBlockEntityContainerMenu<BE>,
            inventory: Inventory,
            title: Component,
        ): HTSingleFluidProcessorScreen<BE> = HTSingleFluidProcessorScreen(
            { createFluidSlot(blockEntity.inputTank, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)) },
            menu,
            inventory,
            title,
        )

        @JvmStatic
        fun enchanter(
            menu: HTBlockEntityContainerMenu<HTEnchantCopierBlockEntity>,
            inventory: Inventory,
            title: Component,
        ): HTSingleFluidProcessorScreen<HTEnchantCopierBlockEntity> = HTSingleFluidProcessorScreen(
            { createFluidSlot(blockEntity.inputTank, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)) },
            createTexture("enchanter"),
            menu,
            inventory,
            title,
        )

        @JvmStatic
        fun <BE : HTItemWithCatalystBlockEntity> itemWithCatalyst(
            menu: HTBlockEntityContainerMenu<BE>,
            inventory: Inventory,
            title: Component,
        ): HTSingleFluidProcessorScreen<BE> = HTSingleFluidProcessorScreen(
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
        ): HTSingleFluidProcessorScreen<HTMelterBlockEntity> = HTSingleFluidProcessorScreen(
            { createFluidTank(blockEntity.outputTank, HTSlotHelper.getSlotPosX(5.5), HTSlotHelper.getSlotPosY(0)) },
            menu,
            inventory,
            title,
        )

        @JvmStatic
        fun <BE : HTSingleItemInputBlockEntity.CachedWithTank<*>> singleItem(
            menu: HTBlockEntityContainerMenu<BE>,
            inventory: Inventory,
            title: Component,
        ): HTSingleFluidProcessorScreen<BE> = HTSingleFluidProcessorScreen(
            { createFluidSlot(blockEntity.inputTank, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2)) },
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
