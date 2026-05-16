package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.cache.HTRecipeCaches
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.handler.HTFluidInputHandler
import hiiragi283.core.impl.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.core.impl.recipe.handler.HTItemOutputHandler
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.recipe.viewer.RagiumRecipeViewerTypes
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.impl.recipe.cache.completed.HTRefiningCompletedRecipe
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTRefineryBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.REFINERY, pos, state) {
    private lateinit var inputTank: HTBasicFluidTank
    private lateinit var firstOutputTank: HTBasicFluidTank
    private lateinit var secondOutputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        inputTank = builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(listener, getTankCapacity()))

        firstOutputTank = builder.addSlot(HTSlotInfo.OUTPUT, HTVariableFluidTank.output(listener, getTankCapacity()))
        secondOutputTank = builder.addSlot(HTSlotInfo.EXTRA_OUTPUT, HTVariableFluidTank.output(listener, getTankCapacity()))
    }

    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        widgetHolder += HTEnergySlotWidget(battery, HTSlotHelper.getSlotPosX(1), HTSlotHelper.getSlotPosY(1))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(3), RagiumRecipeViewerTypes.REFINING)
        // slots
        widgetHolder += HTFluidWidget.Tank(
            inputTank,
            HTSlotHelper.getSlotPosX(2),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
            false,
        )
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            HTSlotHelper.getSlotPosX(3.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.NONE,
        )

        widgetHolder += HTFluidWidget.Tank(
            firstOutputTank,
            HTSlotHelper.getSlotPosX(5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false,
        )
        widgetHolder += HTFluidWidget.Tank(
            secondOutputTank,
            HTSlotHelper.getSlotPosX(7),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.EXTRA_OUTPUT,
            false,
        )
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            HTSlotHelper.getSlotPosX(3.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.OUTPUT,
        )
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : ProgressHandler<HTRefiningRecipe, HTRefiningCompletedRecipe>() {
        private val cache: HTRecipeCaches.ItemAndFluid<HTRefiningRecipe> = HTRecipeCaches.ItemAndFluid(RagiumRecipeLookups.REFINING)
        private val fluidInputHandler: HTFluidInputHandler by lazy { HTFluidInputHandler(inputTank) }
        private val itemInputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
        private val firstFluidOutputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(firstOutputTank) }
        private val secondFluidOutputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(secondOutputTank) }
        private val itemOutputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun findFirstRecipe(level: ServerLevel, pos: BlockPos): HTRefiningRecipe? = cache.findFirstRecipe(itemInputHandler.getStack(), fluidInputHandler.getStack(), level)

        override fun completeRecipe(recipe: HTRefiningRecipe): HTRefiningCompletedRecipe = HTRefiningCompletedRecipe(recipe, fluidInputHandler, itemInputHandler, firstFluidOutputHandler, secondFluidOutputHandler, itemOutputHandler)

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTRefiningCompletedRecipe) {
            recipe.complete()
            playSound(SoundEvents.LAVA_POP)
        }
    }

    override fun createHandler(): HTProgressHandler<*> = ProgressHandlerImpl()

    override fun getConfig(): HTEnergyConfig = RagiumConfig.COMMON.machine.refinery
}
