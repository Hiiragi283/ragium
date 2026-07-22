package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.base.HTItemToItemRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.recipe.cache.completed.HTSingleToSingleCompletedRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.support.recipe.cache.HTRecipeCaches
import hiiragi283.core.support.recipe.handler.HTItemInputHandler
import hiiragi283.core.support.recipe.handler.HTItemOutputHandler
import hiiragi283.core.support.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.common.storge.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.storge.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class HTItemToItemBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTProcessorBlockEntity.Energized(type, pos, state) {
    protected lateinit var inputSlot: HTBasicItemSlot
        private set
    protected lateinit var outputSlot: HTBasicItemSlot
        private set

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, createInputSlot(listener))
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    protected open fun createInputSlot(listener: HTContentListener): HTBasicItemSlot = HTBasicItemSlot.input(listener)

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4), getViewerTypes())
        // slots
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0.5),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(inputSlot)

        widgetHolder += HTItemWidget.Container(
            outputSlot,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputSlot)
    }

    protected open fun getViewerTypes(): Iterable<HTRecipeViewerType<*>> = emptyList()

    //    Basic    //

    abstract class Basic(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTItemToItemBlockEntity(type, pos, state) {
        //    Processing    //

        protected inner class SingleProgressHandler : ProgressHandler<HTItemToItemRecipe, HTSingleToSingleCompletedRecipe.ItemToItem>() {
            private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
            private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

            override fun findFirstRecipe(level: ServerLevel, pos: BlockPos): HTItemToItemRecipe? = getCache().findFirstRecipe(inputHandler.getStack(), level)

            override fun completeRecipe(recipe: HTItemToItemRecipe): HTSingleToSingleCompletedRecipe.ItemToItem = HTSingleToSingleCompletedRecipe.ItemToItem(recipe, inputHandler, outputHandler)

            override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTSingleToSingleCompletedRecipe.ItemToItem) {
                recipe.complete()
                playSound()
            }
        }

        final override fun createHandler(): HTProgressHandler<*> = SingleProgressHandler()

        protected abstract fun getCache(): HTRecipeCaches.SingleItem<out HTItemToItemRecipe>

        protected abstract fun playSound()
    }

    //    Basic    //

    abstract class Simple(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : Basic(type, pos, state) {
        private val cache: HTRecipeCaches.SingleItem<out HTItemToItemRecipe> = HTRecipeCaches.SingleItem(getLookup())

        protected abstract fun getLookup(): HTRecipeLookup<HTItemToItemRecipe>

        override fun getCache(): HTRecipeCaches.SingleItem<out HTItemToItemRecipe> = cache
    }
}
