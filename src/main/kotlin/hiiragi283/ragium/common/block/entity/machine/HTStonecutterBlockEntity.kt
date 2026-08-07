package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.core.api.HTContentListener
import hiiragi283.core.api.gui.HTBackgroundType
import hiiragi283.core.api.gui.HTSlotHelper
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.recipe.cache.completed.HTDoubleInputCompletedRecipe
import hiiragi283.core.api.recipe.handler.HTProgressHandler
import hiiragi283.core.api.recipe.id
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.progress.HTProgressData
import hiiragi283.core.api.recipe.recipe
import hiiragi283.core.common.gui.widget.HTItemWidget
import hiiragi283.core.support.recipe.cache.HTRecipeCaches
import hiiragi283.core.support.recipe.handler.HTItemInputHandler
import hiiragi283.core.support.recipe.handler.HTItemOutputHandler
import hiiragi283.core.support.storage.item.HTBasicItemSlot
import hiiragi283.ragium.common.block.entity.HTProcessorBlockEntity
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.mixin.SingleItemRecipeAccessor
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.support.storage.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.support.storage.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.block.state.BlockState

class HTStonecutterBlockEntity(pos: BlockPos, state: BlockState) : HTProcessorBlockEntity.Energized(RagiumBlockEntityTypes.AUTO_CHISEL.get(), pos, state) {
    private lateinit var inputSlot: HTBasicItemSlot
    private lateinit var catalystSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: HTContentListener) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))

        catalystSlot = builder.addSlot(HTSlotInfo.NONE, HTBasicItemSlot.input(listener))

        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1))
        // progress
        addProgressBar(widgetHolder, HTSlotHelper.getSlotPosX(4))
        // slots
        widgetHolder += HTItemWidget.Container(
            inputSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(inputSlot)
        widgetHolder += HTItemWidget.Container(
            catalystSlot,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.NONE,
        )
        widgetHolder.track(catalystSlot)

        widgetHolder += HTItemWidget.Container(
            outputSlot,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputSlot)
    }

    //    Processing    //

    private inner class ProgressHandlerImpl : ProgressHandler<HTDoubleItemToItemRecipe, HTDoubleInputCompletedRecipe.DoubleItem>() {
        private val cache: HTRecipeCaches.DoubleItem<WrappedRecipe> = HTRecipeCaches.DoubleItem { context: HTRecipeLookup.Context -> context.getAllRecipes(RecipeType.STONECUTTING).associate { it.id to WrappedRecipe(it.recipe) } }
        private val inputHandler: HTItemInputHandler by lazy { HTItemInputHandler(inputSlot) }
        private val catalystHandler: HTItemInputHandler by lazy { HTItemInputHandler(catalystSlot) }
        private val outputHandler: HTItemOutputHandler by lazy { HTItemOutputHandler.single(outputSlot) }

        override fun findFirstRecipe(level: ServerLevel, pos: BlockPos): HTDoubleItemToItemRecipe? = cache.findFirstRecipe(inputHandler.getStack(), catalystHandler.getStack(), level)

        override fun completeRecipe(recipe: HTDoubleItemToItemRecipe): HTDoubleInputCompletedRecipe.DoubleItem = HTDoubleInputCompletedRecipe.DoubleItem(recipe, inputHandler, catalystHandler, outputHandler)

        override fun onComplete(level: ServerLevel, pos: BlockPos, recipe: HTDoubleInputCompletedRecipe.DoubleItem) {
            recipe.complete()
            playSound(SoundEvents.UI_STONECUTTER_TAKE_RESULT)
        }
    }

    override fun createHandler(): HTProgressHandler = ProgressHandlerImpl()

    private class WrappedRecipe(recipe: StonecutterRecipe) : HTDoubleItemToItemRecipe {
        private val accessor: SingleItemRecipeAccessor = recipe as SingleItemRecipeAccessor
        private val ingredient = HTItemIngredient(accessor.ingredient, 1)

        override fun test(first: ItemStack, second: ItemStack): Boolean = ingredient.test(first) && ItemStack.isSameItemSameComponents(accessor.result, second)

        override fun getMatchingStacks(first: ItemStack, second: ItemStack): Pair<ItemStack, ItemStack> = ingredient.getMatchingStack(first) to ItemStack.EMPTY

        override fun assemble(firstInput: ItemStack, secondInput: ItemStack): ItemStack = accessor.result.copy()

        override fun getProgressData(firstInput: ItemStack, secondInput: ItemStack): HTProgressData = HTProgressData.time(5)

        override fun isIncomplete(): Boolean = ingredient.isIncomplete()
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.autoChisel
}
