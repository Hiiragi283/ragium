package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlotHelper
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTDoubleItemToItemBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTDoubleProcessorBlockEntity<ItemInstance, ItemInstance, ItemStack, HTDoubleItemToItemRecipe>(type, pos, state) {

    protected lateinit var outputSlot: HTBasicItemSlot
        private set

    protected fun createOutputSlot(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        outputSlot = builder.addSlot(HTSlotInfo.OUTPUT, HTBasicItemSlot.output(listener))
    }

    //    HTDoubleProcessorBlockEntity    //

    override val recipeCache: HTRecipeCacheHelper.DoubleItem<HTDoubleItemToItemRecipe> = HTRecipeCacheHelper.DoubleItem(getLookup(), true)

    protected abstract fun getLookup(): HTRecipeLookup<HTDoubleItemToItemRecipe>

    private val firstInputHandler: HTInputSlot<ItemInstance> = createFirstInputHandler()
    private val secondInputHandler: HTInputSlot<ItemInstance> = createSecondInputHandler()
    private val outputHandler: HTOutputSlot<ItemStack> = HTOutputSlotHelper.forItem(outputSlot)

    protected abstract fun createFirstInputHandler(): HTInputSlot<ItemInstance>

    protected abstract fun createSecondInputHandler(): HTInputSlot<ItemInstance>

    override fun getFirstInput(): ItemInstance = firstInputHandler.getStoredInput()

    override fun getSecondInput(): ItemInstance = secondInputHandler.getStoredInput()

    override fun canComplete(recipe: HTDoubleItemToItemRecipe, first: ItemInstance, second: ItemInstance, output: ItemStack): Boolean =
        useTransaction { transaction: Transaction ->
            val (target: ItemInstance, disc: ItemInstance) = recipe.getMatchingStack(first, second)
            when {
                firstInputHandler.use(target, transaction).failed -> false
                secondInputHandler.use(disc, transaction).failed -> false
                else -> outputHandler.take(output, transaction) == HTOutputSlot.TakeResult.FULL
            }
        }

    override fun getMaxRecipeProgress(recipe: HTDoubleItemToItemRecipe, first: ItemInstance, second: ItemInstance): Int =
        getMaxRecipeProgress(recipe.getProgressData(first, second).time)

    override fun onComplete(recipe: HTDoubleItemToItemRecipe, first: ItemInstance, second: ItemInstance, output: ItemStack) {
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
