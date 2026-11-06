package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.lookup.HTDoubleRecipeLookupHandler
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.lib.transfer.item.HTBasicItemSlot
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTBasicItemSlotHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTItemAndFluidToBlockEntity<OUTPUT : Any, RECIPE : HTItemAndFluidToRecipe<OUTPUT>>(
    type: BlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTDoubleProcessorBlockEntity<ItemInstance, FluidInstance, OUTPUT, RECIPE>(type, pos, state),
    HTDoubleRecipeLookupHandler.ForItemFluid<RECIPE> {

    protected lateinit var inputTank: HTBasicFluidTank
        private set

    protected fun createInputTank(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        inputTank = builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(getTankCapacity(), listener, filter = ::hasRecipeForSecond))
    }

    protected lateinit var inputSlot: HTBasicItemSlot
        private set

    protected fun createInputSlot(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        inputSlot = builder.addSlot(HTSlotInfo.INPUT, HTBasicItemSlot.input(listener, filter = ::hasRecipeForFirst))
    }

    //    HTDoubleProcessorBlockEntity    //

    override val recipeCache: HTRecipeCacheHelper.ItemAndFluidN<RECIPE> = HTRecipeCacheHelper.ItemAndFluidN(getLookup())

    protected abstract fun getLookup(): HTRecipeLookup<RECIPE>

    protected val inputFluidHandler: HTInputSlot<FluidInstance> = HTInputHandlerHelper.forFluid(inputTank)
    protected val inputItemHandler: HTInputSlot<ItemInstance> = HTInputHandlerHelper.forItem(inputSlot)

    override fun getFirstInput(): ItemInstance = inputItemHandler.getStoredInput()

    override fun getSecondInput(): FluidInstance = inputFluidHandler.getStoredInput()

    override fun getMaxRecipeProgress(recipe: RECIPE, first: ItemInstance, second: FluidInstance): Int =
        getMaxRecipeProgress(recipe.getProgressData(first, second).time)

    protected open fun onComplete() {}

    //    SingleOutput    //

    abstract class SingleOutput<OUTPUT : Any, RECIPE : HTItemAndFluidToRecipe<OUTPUT>>(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
        HTItemAndFluidToBlockEntity<OUTPUT, RECIPE>(type, pos, state) {
        protected val outputHandler: HTOutputSlot<OUTPUT> = createOutputHandler()

        protected abstract fun createOutputHandler(): HTOutputSlot<OUTPUT>

        override fun canComplete(recipe: RECIPE, first: ItemInstance, second: FluidInstance, output: OUTPUT): Boolean =
            useTransaction { transaction: Transaction ->
                val (itemInput: ItemInstance, fluidInput: FluidInstance) = recipe.getMatchingStack(first, second)
                when {
                    inputItemHandler.use(itemInput, transaction).failed -> false
                    inputFluidHandler.use(fluidInput, transaction).failed -> false
                    else -> outputHandler.take(output, transaction) == HTOutputSlot.TakeResult.FULL
                }
            }

        override fun onComplete(recipe: RECIPE, first: ItemInstance, second: FluidInstance, output: OUTPUT) {
            useTransaction { transaction: Transaction ->
                val (itemInput: ItemInstance, fluidInput: FluidInstance) = recipe.getMatchingStack(first, second)
                inputItemHandler.use(itemInput, transaction)
                inputFluidHandler.use(fluidInput, transaction)
                outputHandler.take(output, transaction)
                transaction.commit()
            }
            onComplete()
        }
    }
}
