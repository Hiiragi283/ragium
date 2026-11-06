package hiiragi283.ragium.common.block.entity.processor

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTCatalysedFluidToItemRecipe
import hiiragi283.lib.recipe.handler.output.HTOutputHandler
import hiiragi283.lib.recipe.handler.output.HTOutputHandlerHelper
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.ragium.api.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.common.block.entity.processor.base.HTCatalysedFluidToBlockEntity
import hiiragi283.ragium.common.gui.widget.HTFluidWidget
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState

class HTFreezerBlockEntity(pos: BlockPos, state: BlockState) :
    HTCatalysedFluidToBlockEntity.SingleOutput<ItemStack, HTCatalysedFluidToItemRecipe>(RagiumBlockEntityTypes.FREEZER.get(), pos, state) {
    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        createInputTank(builder, listener)
    }

    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        createCatalystSlot(builder, listener)
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder)
        // input
        widgetHolder += HTFluidWidget.Tank(
            inputTank,
            HTSlotHelper.getSlotPosX(1),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
            false,
        )
        widgetHolder.track(inputTank)
        // catalyst
        widgetHolder += HTItemWidget.Container(
            catalystSlot,
            0,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.NONE,
        )
        widgetHolder.track(catalystSlot)
        // output
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            1,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputSlot)
    }

    //    HTCatalysedFluidToItemBlockEntity    //

    override fun createOutputHandler(): HTOutputHandler<ItemStack> = HTOutputHandlerHelper.forItem(outputSlot)

    override fun getLookup(): HTRecipeLookup<HTCatalysedFluidToItemRecipe> = RagiumRecipeLookups.FREEZING

    override fun onComplete() {
        playSound(SoundEvents.BUCKET_FILL_POWDER_SNOW)
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.freezer
}
