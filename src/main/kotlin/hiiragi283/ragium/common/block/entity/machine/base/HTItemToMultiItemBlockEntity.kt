package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeCaches
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.core.common.storage.item.HTBasicItemSlot
import hiiragi283.core.impl.recipe.cache.completed.HTSingleToMultiItemCompletedRecipe
import hiiragi283.core.impl.recipe.handler.HTItemInputHandler
import hiiragi283.core.impl.recipe.handler.HTItemOutputHandler
import hiiragi283.ragium.common.gui.widget.HTEnergySlotWidget
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

abstract class HTItemToMultiItemBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) : HTMultiItemBlockEntity(type, pos, state) {
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
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        setupOutputSlots(widgetHolder)
    }

    protected abstract fun setupOutputSlots(widgetHolder: HTWidgetHolder)

    //    Processing    //

    private inner class ProgressHandlerImpl : ProgressHandler<HTItemToMultiItemRecipe, HTSingleToMultiItemCompletedRecipe.ItemToItem>() {
        private val cache: HTRecipeCaches.SingleItem<out HTItemToMultiItemRecipe> = HTRecipeCaches.SingleItem(getLookup())
        private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.multiple(outputSlots) }

        override fun findFirstRecipe(level: ServerLevel, pos: BlockPos): HTItemToMultiItemRecipe? = cache.findFirstRecipe(inputHandler.getStack(), level)

        override fun completeRecipe(recipe: HTItemToMultiItemRecipe): HTSingleToMultiItemCompletedRecipe.ItemToItem = HTSingleToMultiItemCompletedRecipe.ItemToItem(recipe, inputHandler, outputHandler)

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTSingleToMultiItemCompletedRecipe.ItemToItem) {
            recipe.complete()
            playSound()
        }
    }

    final override fun createHandler(): HTProgressHandler<*> = ProgressHandlerImpl()

    protected abstract fun getLookup(): HTRecipeLookup<out HTItemToMultiItemRecipe>

    protected abstract fun playSound()
}
