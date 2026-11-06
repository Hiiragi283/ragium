package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTCatalysedItemToItemRecipe
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlotHelper
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class HTCatalysedItemToItemBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTCatalysedItemToBlockEntity.SingleOutput<ItemStack, HTCatalysedItemToItemRecipe>(type, pos, state) {
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        createInputSlot(builder, listener)
        createCatalystSlot(builder, listener)
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun createOutputHandler(): HTOutputSlot<ItemStack> = HTOutputSlotHelper.forItem(outputSlot)

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1))
        // progress
        addProgressBar(widgetHolder)
        // inputs
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            0,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(inputSlot)
        widgetHolder += HTItemWidget.Container(
            catalystSlot,
            1,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.NONE,
        )
        widgetHolder.track(catalystSlot)
        // output
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            2,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputSlot)
    }
}
