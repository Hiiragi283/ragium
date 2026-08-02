package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.base.HTItemAndFluidToItemRecipe
import hiiragi283.core.api.recipe.cache.completed.HTDoubleInputCompletedRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.support.recipe.cache.HTRecipeCaches
import hiiragi283.core.support.recipe.handler.HTFluidInputHandler
import hiiragi283.core.support.recipe.handler.HTItemInputHandler
import hiiragi283.core.support.recipe.handler.HTItemOutputHandler
import hiiragi283.core.support.storage.fluid.HTBasicFluidTank
import hiiragi283.core.support.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.support.storage.fluid.HTVariableFluidTank
import hiiragi283.ragium.support.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.support.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.support.storage.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTFreezerBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.FREEZER.get(), pos, state) {
    private lateinit var inputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(listener, getTankCapacity()),
        )
    }

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4), RagiumRecipeViewerTypes.FREEZING)
        // inputs
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(inputSlot)
        widgetHolder += HTFluidWidget.Tank(
            inputTank,
            HTSlotHelper.getSlotPosX(1),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.EXTRA_INPUT,
            false,
        )
        widgetHolder.track(inputTank)
        // output
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputSlot)
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : ProgressHandler<HTItemAndFluidToItemRecipe, HTDoubleInputCompletedRecipe.ItemAndFluid>() {
        private val cache: HTRecipeCaches.ItemAndFluid<HTItemAndFluidToItemRecipe> = HTRecipeCaches.ItemAndFluid(RagiumRecipeLookups.FREEZING)
        private val inputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(inputTank) }
        private val catalystHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun findFirstRecipe(level: ServerLevel, pos: BlockPos): HTItemAndFluidToItemRecipe? = cache.findFirstRecipe(catalystHandler.getStack(), inputHandler.getStack(), level)

        override fun completeRecipe(recipe: HTItemAndFluidToItemRecipe): HTDoubleInputCompletedRecipe.ItemAndFluid = HTDoubleInputCompletedRecipe.ItemAndFluid(recipe, catalystHandler, inputHandler, outputHandler)

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTDoubleInputCompletedRecipe.ItemAndFluid) {
            recipe.complete()
            playSound(SoundEvents.BUCKET_FILL_POWDER_SNOW)
        }
    }

    override fun createHandler(): HTProgressHandler<*> = ProgressHandlerImpl()

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.freezer
}
