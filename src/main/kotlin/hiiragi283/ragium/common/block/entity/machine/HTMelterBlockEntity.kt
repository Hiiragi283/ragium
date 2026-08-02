package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.core.api.recipe.cache.completed.HTSingleToSingleCompletedRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.support.recipe.cache.HTRecipeCaches
import hiiragi283.core.support.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.support.recipe.handler.HTItemInputHandler
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

class HTMelterBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.MELTER.get(), pos, state) {
    private lateinit var outputTank: HTBasicFluidTank

    override fun createFluidTanks(builder: HTBasicFluidTankHolder.Builder, listener: HTContentListener) {
        outputTank = builder.addSlot(
            HTSlotInfo.OUTPUT,
            HTVariableFluidTank.output(listener, getTankCapacity()),
        )
    }

    private lateinit var inputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4), RagiumRecipeViewerTypes.MELTING)
        // input
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(inputSlot)
        // output
        widgetHolder += HTFluidWidget.Tank(
            outputTank,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.OUTPUT,
            false,
        )
        widgetHolder.track(outputTank)
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : ProgressHandler<HTItemToFluidRecipe, HTSingleToSingleCompletedRecipe.ItemToFluid>() {
        private val cache: HTRecipeCaches.SingleItem<HTItemToFluidRecipe> = HTRecipeCaches.SingleItem(RagiumRecipeLookups.MELTING)
        private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
        private val outputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(outputTank) }

        override fun findFirstRecipe(level: ServerLevel, pos: BlockPos): HTItemToFluidRecipe? = cache.findFirstRecipe(inputHandler.getStack(), level)

        override fun completeRecipe(recipe: HTItemToFluidRecipe): HTSingleToSingleCompletedRecipe.ItemToFluid = HTSingleToSingleCompletedRecipe.ItemToFluid(recipe, inputHandler, outputHandler)

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTSingleToSingleCompletedRecipe.ItemToFluid) {
            recipe.complete()
            playSound(SoundEvents.LAVA_POP)
        }
    }

    override fun createHandler(): HTProgressHandler<*> = ProgressHandlerImpl()

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.melter
}
