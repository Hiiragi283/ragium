package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.gui.HTBackgroundType
import hiiragi283.lib.gui.HTSlotHelper
import hiiragi283.lib.gui.widget.HTWidgetHolder
import hiiragi283.lib.recipe.base.HTItemCombineRecipe
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.lookup.HTDoubleRecipeLookupHandler
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

abstract class HTItemCombineBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTDoubleProcessorBlockEntity<ItemInstance, ItemInstance, ItemStack, HTItemCombineRecipe>(type, pos, state),
    HTDoubleRecipeLookupHandler.ForItemCombine {

    private lateinit var topInputSlot: HTBasicItemSlot
    private lateinit var bottomInputSlot: HTBasicItemSlot
    private lateinit var outputSlot: HTBasicItemSlot

    override fun createItemSlots(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        topInputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener, filter = ::hasRecipeForFirst))
        bottomInputSlot = builder.addSlot(HTSlotInfo.EXTRA_INPUT, HTBasicItemSlot.input(listener, filter = ::hasRecipeForSecond))
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    override fun setupMenu(widgetHolder: HTWidgetHolder) {
        super.setupMenu(widgetHolder)
        addEnergySlot(widgetHolder, HTSlotHelper.getSlotPosX(2.5), HTSlotHelper.getSlotPosY(1))
        // progress
        addProgressBar(widgetHolder)
        // inputs
        widgetHolder += HTItemWidget.Container(
            topInputSlot,
            0,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(0),
            HTBackgroundType.INPUT,
        )
        widgetHolder.track(topInputSlot)
        widgetHolder += HTItemWidget.Container(
            bottomInputSlot,
            1,
            HTSlotHelper.getSlotPosX(2.5),
            HTSlotHelper.getSlotPosY(2),
            HTBackgroundType.EXTRA_INPUT,
        )
        widgetHolder.track(bottomInputSlot)
        // output
        widgetHolder += HTItemWidget.Container(
            outputSlot,
            2,
            HTSlotHelper.getSlotPosX(6),
            HTSlotHelper.getSlotPosY(1),
            HTBackgroundType.OUTPUT,
        )
        widgetHolder.track(outputSlot)
    }

    //    HTDoubleProcessorBlockEntity    //

    override val recipeCache: HTRecipeCacheHelper.ItemCombine = HTRecipeCacheHelper.ItemCombine(getLookup())

    protected abstract fun getLookup(): HTRecipeLookup<HTItemCombineRecipe>

    private val firstInputHandler: HTInputSlot<ItemInstance> = HTInputHandlerHelper.forItem(topInputSlot)
    private val secondInputHandler: HTInputSlot<ItemInstance> = HTInputHandlerHelper.forItem(bottomInputSlot)
    private val outputHandler: HTOutputHandler<ItemStack> = HTOutputHandlerHelper.forItem(outputSlot)

    override fun getFirstInput(): ItemInstance = firstInputHandler.getStoredInput()

    override fun getSecondInput(): ItemInstance = secondInputHandler.getStoredInput()

    override fun canComplete(recipe: HTItemCombineRecipe, first: ItemInstance, second: ItemInstance, output: ItemStack): Boolean =
        useTransaction { transaction: Transaction ->
            val (target: ItemInstance, disc: ItemInstance) = recipe.getMatchingStack(first, second)
            when {
                firstInputHandler.use(target, transaction).failed -> false
                secondInputHandler.use(disc, transaction).failed -> false
                else -> outputHandler.take(output, transaction) == HTOutputHandler.TakeResult.FULL
            }
        }

    override fun getMaxRecipeProgress(recipe: HTItemCombineRecipe, first: ItemInstance, second: ItemInstance): Int =
        getMaxRecipeProgress(recipe.getProgressData(first, second).time)

    override fun onComplete(recipe: HTItemCombineRecipe, first: ItemInstance, second: ItemInstance, output: ItemStack) {
        useTransaction { transaction: Transaction ->
            val (target: ItemInstance, disc: ItemInstance) = recipe.getMatchingStack(first, second)
            firstInputHandler.use(target, transaction)
            secondInputHandler.use(disc, transaction)
            outputHandler.take(output, transaction)
            transaction.commit()
        }
        onComplete()
    }

    protected open fun onComplete() {}
}
