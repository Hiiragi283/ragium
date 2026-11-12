package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.recipe.base.HTFluidToRecipe
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.lookup.HTSingleRecipeLookupHandler
import hiiragi283.lib.recipe.handler.output.HTOutputHandler
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.transfer.fluid.HTBasicFluidTank
import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.common.transfer.fluid.HTVariableFluidTank
import hiiragi283.ragium.common.transfer.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.transfer.holder.HTSlotInfo
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.transfer.transaction.Transaction

abstract class HTFluidToBlockEntity<OUTPUT : Any, RECIPE : HTFluidToRecipe<OUTPUT>>(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTSingleProcessorBlockEntity<FluidInstance, OUTPUT, RECIPE>(type, pos, state),
    HTSingleRecipeLookupHandler.ForFluid<RECIPE> {
    protected lateinit var inputTank: HTBasicFluidTank
        private set

    protected fun createInputTank(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        inputTank = builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(getTankCapacity(), listener, filter = ::hasRecipe))
    }

    //    HTSingleProcessorBlockEntity    //

    final override val recipeCache: HTRecipeCacheHelper.SingleFluid<RECIPE> = HTRecipeCacheHelper.SingleFluid(getLookup())

    protected abstract fun getLookup(): HTRecipeLookup<RECIPE>

    protected val inputHandler: HTInputSlot<FluidInstance> = HTInputHandlerHelper.forFluid(inputTank)

    override fun getInput(): FluidInstance = inputHandler.getStoredInput()

    override fun getMaxRecipeProgress(recipe: RECIPE, input: FluidInstance): Int = getMaxRecipeProgress(recipe.getProgressData(input).time)

    protected open fun onComplete() {}

    //    SingleOutput    //

    abstract class SingleOutput<OUTPUT : Any, RECIPE : HTFluidToRecipe<OUTPUT>>(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
        HTFluidToBlockEntity<OUTPUT, RECIPE>(type, pos, state) {
        protected val outputHandler: HTOutputHandler<OUTPUT> = createOutputHandler()

        protected abstract fun createOutputHandler(): HTOutputHandler<OUTPUT>

        override fun canComplete(recipe: RECIPE, input: FluidInstance, output: OUTPUT): Boolean = useTransaction { transaction: Transaction ->
            when {
                inputHandler.use(recipe.getMatchingStack(input), transaction).failed -> false
                else -> outputHandler.take(output, transaction) == HTOutputHandler.TakeResult.FULL
            }
        }

        override fun onComplete(recipe: RECIPE, input: FluidInstance, output: OUTPUT) {
            useTransaction { transaction: Transaction ->
                inputHandler.use(recipe.getMatchingStack(input), transaction)
                outputHandler.take(output, transaction)
                transaction.commit()
            }
            onComplete()
        }
    }
}
