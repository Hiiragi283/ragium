package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.base.HTSingleMultiOutputRecipe
import hiiragi283.core.api.recipe.handler.HTHandledRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.common.gui.widget.HTItemSlotWidget
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

abstract class HTSingleMultiOutputBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTMultiOutputBlockEntity<SingleRecipeInput, HTSingleMultiOutputRecipe>(type, pos, state) {
    private lateinit var inputSlot: HTBasicItemSlot

    final override fun createInputSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
    }

    final override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        widgetHolder += HTEnergySlotWidget(battery, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4))
        // slots
        widgetHolder += HTItemSlotWidget.container(
            inputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        setupOutputSlots(widgetHolder)
    }

    protected abstract fun setupOutputSlots(widgetHolder: HTWidgetHolder)

    //    Processing    //

    private inner class ProgressHandlerImpl : MultiOutputProgressHandler() {
        private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }

        override fun createInput(level: ServerLevel, pos: BlockPos): SingleRecipeInput? = createInput(inputHandler)

        override fun completeInput(
            level: ServerLevel,
            pos: BlockPos,
            recipe: HTHandledRecipe<SingleRecipeInput, HTSingleMultiOutputRecipe>,
        ) {
            inputHandler.consume(recipe.map(HTSingleMultiOutputRecipe::getRequiredAmount))
            playSound()
        }
    }

    final override fun createHandler(): HTProgressHandler<*> = ProgressHandlerImpl()

    protected abstract fun playSound()
}
