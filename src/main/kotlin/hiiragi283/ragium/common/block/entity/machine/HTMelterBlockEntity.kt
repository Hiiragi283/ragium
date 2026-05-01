package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeCaches
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.common.gui.widget.HTFluidWidget
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.storage.fluid.HTBasicFluidTank
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.cache.completed.HTSingleToSingleCompletedRecipe
import hiiragi283.core.impl.recipe.handler.HTFluidOutputHandler
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.recipe.RagiumRecipeLookups
import hiiragi283.ragium.common.storge.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.storge.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTMelterBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.MELTER, pos, state) {
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
        widgetHolder += HTEnergySlotWidget(battery, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4))
        // input
        widgetHolder += HTItemSlotWidget.container(
            inputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        // output
        widgetHolder += HTFluidWidget
            .createTank(
                outputTank,
                HTSlotHelper.getSlotPosX(6),
                HTSlotHelper.getSlotPosY(0),
            ).setBackground(HTBackgroundType.OUTPUT)
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : ProgressHandler<HTItemToFluidRecipe, HTSingleToSingleCompletedRecipe.ItemToFluid>() {
        private val cache: HTRecipeCaches.SingleItem<HTItemToFluidRecipe> = HTRecipeCaches.SingleItem(RagiumRecipeLookups.MELTING)
        private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
        private val outputHandler: HTFluidOutputHandler by lazy { HTFluidOutputHandler.single(outputTank) }

        override fun findFirstRecipe(level: ServerLevel, pos: BlockPos): HTItemToFluidRecipe? =
            cache.findFirstRecipe(inputHandler.getStack(), level)

        override fun completeRecipe(recipe: HTItemToFluidRecipe): HTSingleToSingleCompletedRecipe.ItemToFluid =
            HTSingleToSingleCompletedRecipe.ItemToFluid(recipe, inputHandler, outputHandler)

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTSingleToSingleCompletedRecipe.ItemToFluid) {
            recipe.complete()
            playSound(SoundEvents.LAVA_POP)
        }
    }

    override fun createHandler(): HTProgressHandler<*> = ProgressHandlerImpl()

    override fun getConfig(): HTEnergyConfig = RagiumConfig.COMMON.machine.melter
}
