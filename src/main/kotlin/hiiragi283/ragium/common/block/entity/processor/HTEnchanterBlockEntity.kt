package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.HTConstants
import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTDoubleItemToItemBlockEntity
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.gui.widget.HTTextureWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.level.block.state.BlockState

class HTEnchanterBlockEntity(pos: BlockPos, state: BlockState) : HTDoubleItemToItemBlockEntity(RagiumBlockEntityTypes.ENCHANTER.get(), pos, state) {
    private lateinit var primarySlot: HTBasicItemSlot
    private lateinit var secondarySlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        primarySlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
        secondarySlot = builder.addSlot(HTSlotInfo.EXTRA_INPUT, HTBasicItemSlot.input(listener))
        createOutputSlot(builder, listener)
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(1.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder)
        // inputs
        widgetHolder += HTItemWidget.Container(
            primarySlot,
            0,
            HTSlotHelper.getSlotPosX(0.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(primarySlot)
        widgetHolder += HTTextureWidget(
            RagiumAPI.id(HTConstants.TEXTURES, HTConstants.GUI, "plus"),
            HTSlotHelper.getSlotPosX(1.5),
            HTSlotHelper.getSlotPosY(0.5),
            18,
            18,
        )
        widgetHolder += HTItemWidget.Container(
            secondarySlot,
            1,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.EXTRA_INPUT,
        )
        widgetHolder.track(secondarySlot)
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

    //    HTDoubleItemToItemBlockEntity    //

    override fun getLookup(): HTRecipeLookup<HTDoubleItemToItemRecipe> = RagiumRecipeLookups.ENCHANTING

    override fun createFirstInputHandler(): HTInputSlot<ItemInstance> = HTInputHandlerHelper.forItem(primarySlot)

    override fun createSecondInputHandler(): HTInputSlot<ItemInstance> = HTInputHandlerHelper.forItem(secondarySlot)

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.enchanter
}
