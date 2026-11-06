package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.recipe.base.HTItemToRecipe
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.lookup.HTSingleRecipeLookupHandler
import hiiragi283.lib.recipe.handler.output.HTOutputHandler
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

abstract class HTItemToBlockEntity<OUTPUT : Any, RECIPE : HTItemToRecipe<OUTPUT>>(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTSingleProcessorBlockEntity<ItemInstance, OUTPUT, RECIPE>(type, pos, state),
    HTSingleRecipeLookupHandler.ForItem<RECIPE> {
    protected lateinit var inputSlot: HTBasicItemSlot
        private set

    protected fun createInputSlot(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener, filter = ::hasRecipe))
    }

    //    HTSingleProcessorBlockEntity    //

    final override val recipeCache: HTRecipeCacheHelper.SingleItem<RECIPE> = HTRecipeCacheHelper.SingleItem(getLookup())

    protected abstract fun getLookup(): HTRecipeLookup<RECIPE>

    protected val inputHandler: HTInputSlot<ItemInstance> = HTInputHandlerHelper.forItem(inputSlot)

    override fun getInput(): ItemInstance = inputHandler.getStoredInput()

    override fun getMaxRecipeProgress(recipe: RECIPE, input: ItemInstance): Int = getMaxRecipeProgress(recipe.getProgressData(input).time)

    protected open fun onComplete() {}

    //    SingleOutput    //

    abstract class SingleOutput<OUTPUT : Any, RECIPE : HTItemToRecipe<OUTPUT>>(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
        HTItemToBlockEntity<OUTPUT, RECIPE>(type, pos, state) {
        protected val outputHandler: HTOutputHandler<OUTPUT> = createOutputHandler()

        protected abstract fun createOutputHandler(): HTOutputHandler<OUTPUT>

        override fun canComplete(recipe: RECIPE, input: ItemInstance, output: OUTPUT): Boolean = useTransaction { transaction: Transaction ->
            when {
                inputHandler.use(recipe.getMatchingStack(input), transaction).failed -> false
                else -> outputHandler.take(output, transaction) == HTOutputHandler.TakeResult.FULL
            }
        }

        override fun onComplete(recipe: RECIPE, input: ItemInstance, output: OUTPUT) {
            useTransaction { transaction: Transaction ->
                inputHandler.use(recipe.getMatchingStack(input), transaction)
                outputHandler.take(output, transaction)
                transaction.commit()
            }
            onComplete()
        }
    }
}
