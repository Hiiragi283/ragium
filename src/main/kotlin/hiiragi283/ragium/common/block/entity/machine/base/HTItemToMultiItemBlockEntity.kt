package hiiragi283.ragium.common.block.entity.machine.base

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.recipe.cache.completed.HTSingleToMultiItemCompletedRecipe
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.sounds.HTSoundInstance
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.support.recipe.cache.HTRecipeCaches
import hiiragi283.core.support.recipe.handler.HTItemInputHandler
import hiiragi283.core.support.recipe.handler.HTItemOutputHandler
import hiiragi283.core.support.storage.item.HTBasicItemSlot
import hiiragi283.ragium.support.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.support.storage.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class HTItemToMultiItemBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : HTMultiItemBlockEntity(type, pos, state) {
    private lateinit var inputSlot: HTBasicItemSlot

    final override fun createInputSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
    }

    final override fun setupMenu(widgetHolder: HTWidgetHolder) {
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

        setupOutputSlots(widgetHolder)
    }

    protected abstract fun setupOutputSlots(widgetHolder: HTWidgetHolder)

    protected open fun getViewerTypes(): Iterable<HTRecipeViewerType<*>> = emptyList()

    //    Processing    //

    protected inner class ItemToMultiItemProgressHandler : SimpleProgressHandler<HTItemToMultiItemRecipe, HTSingleToMultiItemCompletedRecipe.ItemToItem> {
        constructor(sound: HTSoundInstance) : super(sound)

        constructor(sound: SoundEvent) : super(sound)

        private val cache: HTRecipeCaches.SingleItem<out HTItemToMultiItemRecipe> = HTRecipeCaches.SingleItem(getLookup())
        private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.multiple(outputSlots) }

        override fun findFirstRecipe(level: ServerLevel, pos: BlockPos): HTItemToMultiItemRecipe? = cache.findFirstRecipe(inputHandler.getStack(), level)

        override fun completeRecipe(recipe: HTItemToMultiItemRecipe): HTSingleToMultiItemCompletedRecipe.ItemToItem = HTSingleToMultiItemCompletedRecipe.ItemToItem(recipe, inputHandler, outputHandler)
    }

    protected abstract fun getLookup(): HTRecipeLookup<HTItemToMultiItemRecipe>
}
