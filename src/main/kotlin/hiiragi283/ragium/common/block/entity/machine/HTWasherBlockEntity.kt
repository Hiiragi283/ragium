package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.support.recipe.cache.HTRecipeCaches
import hiiragi283.core.support.recipe.handler.HTFluidInputHandler
import hiiragi283.core.support.recipe.handler.HTItemInputHandler
import hiiragi283.core.support.recipe.handler.HTItemOutputHandler
import hiiragi283.core.support.storage.fluid.HTBasicFluidTank
import hiiragi283.core.support.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.machine.base.HTMultiItemBlockEntity
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.recipe.cache.completed.HTWashingCompletedRecipe
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

class HTWasherBlockEntity(pos: BlockPos, state: BlockState) : HTMultiItemBlockEntity(RagiumBlockEntityTypes.WASHER.get(), pos, state) {
    private lateinit var inputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank = builder.addSlot(
            HTSlotInfo.INPUT,
            HTVariableFluidTank.input(listener, getTankCapacity()),
        )
    }

    private lateinit var inputSlot: HTBasicItemSlot

    override fun createInputSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
    }

    override fun getOutputSlotSize(): Int = 4

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4), RagiumRecipeViewerTypes.WASHING)
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
        // outputs
        outputSlots
            .onEach { widgetHolder.track(it) }
            .mapIndexed { index: Int, slot: HTBasicItemSlot ->
                HTItemWidget.Container(
                    slot,
                    HTSlotHelper.getSlotPosX(6 + index % 2),
                    HTSlotHelper.getSlotPosY(0.5 + index / 2),
                    HTBackgroundType.OUTPUT,
                )
            }.forEach(widgetHolder::addWidget)
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : ProgressHandler<HTWashingRecipe, HTWashingCompletedRecipe>() {
        private val cache: HTRecipeCaches.ItemAndFluid<HTWashingRecipe> = HTRecipeCaches.ItemAndFluid(RagiumRecipeLookups.WASHING)
        private val itemInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
        private val fluidInputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(inputTank) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.multiple(outputSlots) }

        override fun findFirstRecipe(level: ServerLevel, pos: BlockPos): HTWashingRecipe? = cache.findFirstRecipe(itemInputHandler.getStack(), fluidInputHandler.getStack(), level)

        override fun completeRecipe(recipe: HTWashingRecipe): HTWashingCompletedRecipe = HTWashingCompletedRecipe(recipe, itemInputHandler, fluidInputHandler, outputHandler)

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTWashingCompletedRecipe) {
            recipe.complete()
            playSound(SoundEvents.BUBBLE_COLUMN_UPWARDS_INSIDE)
        }
    }

    override fun createHandler(): HTProgressHandler<*> = ProgressHandlerImpl()

    override fun getConfig(): HTEnergyConfig = RagiumConfig.COMMON.machine.washer
}
