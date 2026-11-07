package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTSizedShapelessRecipe
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.input.HTInputHandler
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.output.HTOutputHandler
import hiiragi283.lib.recipe.handler.output.HTOutputHandlerHelper
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.common.gui.widget.HTItemWidget
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTSizedShapelessBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTSingleProcessorBlockEntity<List<ItemInstance>, ItemStack, HTSizedShapelessRecipe>(type, pos, state) {

    protected lateinit var inputSlots: List<HTBasicItemSlot>
        private set
    protected lateinit var outputSlot: HTBasicItemSlot
        private set

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlots = List(3) { builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener)) }
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(1.5), HTSlotHelper.getSlotPosY(1.5))
        // progress
        addProgressBar(widgetHolder)
        // inputs
        inputSlots.forEachIndexed { index: Int, slot: HTBasicItemSlot ->
            widgetHolder += HTItemWidget.Container(
                slot,
                index,
                HTSlotHelper.getSlotPosX(0.5 + index),
                HTSlotHelper.getSlotPosY(0.5),
                HTBackgroundType.INPUT,
            )
            widgetHolder.track(slot)
        }
        // output
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            3,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputSlot)
    }

    //    HTSingleProcessorBlockEntity    //

    final override val recipeCache: HTRecipeCacheHelper.MultiItem<HTSizedShapelessRecipe> = HTRecipeCacheHelper.MultiItem(getLookup())

    protected abstract fun getLookup(): HTRecipeLookup<HTSizedShapelessRecipe>

    private val inputHandler: HTInputHandler<ItemInstance> = HTInputHandlerHelper.forItems(inputSlots)
    private val outputHandler: HTOutputHandler<ItemStack> = HTOutputHandlerHelper.forItem(outputSlot)

    override fun getInput(): List<ItemInstance> = inputSlots.map(HTBasicItemSlot::getStackCopy)

    override fun canComplete(recipe: HTSizedShapelessRecipe, input: List<ItemInstance>, output: ItemStack): Boolean =
        useTransaction { transaction: Transaction ->
            when {
                recipe.getMatchingStack(input).any { inputHandler.use(it, transaction).failed } -> false
                else -> outputHandler.take(output, transaction).fullOrNoneTaken
            }
        }

    override fun getMaxRecipeProgress(recipe: HTSizedShapelessRecipe, input: List<ItemInstance>): Int =
        getMaxRecipeProgress(recipe.getProgressData(input).time)

    override fun onComplete(recipe: HTSizedShapelessRecipe, input: List<ItemInstance>, output: ItemStack) {
        useTransaction { transaction: Transaction ->
            recipe.getMatchingStack(input).forEach { inputHandler.use(it, transaction) }
            outputHandler.take(output, transaction)
            transaction.commit()
        }
        onComplete()
    }

    protected open fun onComplete() {}
}
