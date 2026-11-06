package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.recipe.base.HTCatalysedItemToRecipe
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTCatalysedItemToBlockEntity<OUTPUT : Any, RECIPE : HTCatalysedItemToRecipe<OUTPUT>>(
    type: BlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTDoubleProcessorBlockEntity<ItemInstance, ItemInstance, OUTPUT, RECIPE>(type, pos, state) {
    protected lateinit var inputSlot: HTBasicItemSlot
        private set
    protected lateinit var catalystSlot: HTBasicItemSlot
        private set

    protected fun createInputSlot(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener))
    }

    protected fun createCatalystSlot(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        catalystSlot = builder.addSlot(HTSlotInfo.NONE, HTBasicItemSlot.catalyst(listener))
    }

    //    HTDoubleProcessorBlockEntity    //

    final override val recipeCache: HTRecipeCacheHelper.CatalysedItem<RECIPE> = HTRecipeCacheHelper.CatalysedItem(getLookup())

    protected abstract fun getLookup(): HTRecipeLookup<RECIPE>

    protected val inputHandler: HTInputSlot<ItemInstance> = HTInputHandlerHelper.forItem(inputSlot)

    override fun getFirstInput(): ItemInstance = inputHandler.getStoredInput()

    override fun getSecondInput(): ItemInstance = catalystSlot.getStackCopy()

    override fun getMaxRecipeProgress(recipe: RECIPE, first: ItemInstance, second: ItemInstance): Int =
        getMaxRecipeProgress(recipe.getProgressData(first, second).time)

    protected open fun onComplete() {}

    //    SingleOutput    //

    abstract class SingleOutput<OUTPUT : Any, RECIPE : HTCatalysedItemToRecipe<OUTPUT>>(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
        HTCatalysedItemToBlockEntity<OUTPUT, RECIPE>(type, pos, state) {
        protected val outputHandler: HTOutputSlot<OUTPUT> = createOutputHandler()

        protected abstract fun createOutputHandler(): HTOutputSlot<OUTPUT>

        override fun canComplete(recipe: RECIPE, first: ItemInstance, second: ItemInstance, output: OUTPUT): Boolean =
            useTransaction { transaction: Transaction ->
                when {
                    inputHandler.use(recipe.getMatchingStack(first), transaction).failed -> false
                    else -> outputHandler.take(output, transaction) == HTOutputSlot.TakeResult.FULL
                }
            }

        override fun onComplete(recipe: RECIPE, first: ItemInstance, second: ItemInstance, output: OUTPUT) {
            useTransaction { transaction: Transaction ->
                inputHandler.use(recipe.getMatchingStack(first), transaction)
                outputHandler.take(output, transaction)
                transaction.commit()
            }
            onComplete()
        }
    }
}
