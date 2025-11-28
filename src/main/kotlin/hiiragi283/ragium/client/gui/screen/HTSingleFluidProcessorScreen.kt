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
import net.minecraft.world.entity.player.Inventory
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTSingleFluidProcessorScreen<BE : HTEnergizedProcessorBlockEntity<*, *>>(
    private val factory: HTSingleFluidProcessorScreen<BE>.() -> HTFluidTankWidget,
    menu: HTBlockEntityContainerMenu<BE>,
    inventory: Inventory,
    title: Component,
) : HTProcessorScreen<BE>(menu, inventory, title) {
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
        fun enchCopier(
            menu: HTBlockEntityContainerMenu<HTEnchantCopierBlockEntity>,
            inventory: Inventory,
            title: Component,
        ): HTSingleFluidProcessorScreen<HTEnchantCopierBlockEntity> = HTSingleFluidProcessorScreen(
            { createFluidTank(blockEntity.inputTank, HTSlotHelper.getSlotPosX(1), HTSlotHelper.getSlotPosY(0)) },
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
