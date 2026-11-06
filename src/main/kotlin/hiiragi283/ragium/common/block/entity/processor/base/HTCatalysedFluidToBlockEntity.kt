package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.recipe.base.HTCatalysedFluidToRecipe
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.input.HTInputHandlerHelper
import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputHandler
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

abstract class HTCatalysedFluidToBlockEntity<OUTPUT : Any, RECIPE : HTCatalysedFluidToRecipe<OUTPUT>>(
    type: BlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTDoubleProcessorBlockEntity<FluidInstance, ItemInstance, OUTPUT, RECIPE>(type, pos, state) {
    protected lateinit var inputTank: HTBasicFluidTank
        private set

    protected fun createInputTank(builder: HTBasicFluidTankHolder.Builder, listener: Runnable) {
        inputTank = builder.addSlot(HTSlotInfo.INPUT, HTVariableFluidTank.input(getTankCapacity(), listener))
    }

    protected lateinit var catalystSlot: HTBasicItemSlot
        private set

    protected fun createCatalystSlot(builder: HTBasicItemSlotHolder.Builder, listener: Runnable) {
        catalystSlot = builder.addSlot(HTSlotInfo.NONE, HTBasicItemSlot.catalyst(listener))
    }

    //    HTDoubleProcessorBlockEntity    //

    final override val recipeCache: HTRecipeCacheHelper.CatalysedFluid<RECIPE> = HTRecipeCacheHelper.CatalysedFluid(getLookup())

    protected abstract fun getLookup(): HTRecipeLookup<RECIPE>

    protected val inputHandler: HTInputSlot<FluidInstance> = HTInputHandlerHelper.forFluid(inputTank)

    override fun getFirstInput(): FluidInstance = inputHandler.getStoredInput()

    override fun getSecondInput(): ItemInstance = catalystSlot.getStackCopy()

    override fun getMaxRecipeProgress(recipe: RECIPE, first: FluidInstance, second: ItemInstance): Int =
        getMaxRecipeProgress(recipe.getProgressData(first, second).time)

    protected open fun onComplete() {}

    //    SingleOutput    //

    abstract class SingleOutput<OUTPUT : Any, RECIPE : HTCatalysedFluidToRecipe<OUTPUT>>(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
        HTCatalysedFluidToBlockEntity<OUTPUT, RECIPE>(type, pos, state) {
        protected val outputHandler: HTOutputHandler<OUTPUT> = createOutputHandler()

        protected abstract fun createOutputHandler(): HTOutputHandler<OUTPUT>

        override fun canComplete(recipe: RECIPE, first: FluidInstance, second: ItemInstance, output: OUTPUT): Boolean =
            useTransaction { transaction: Transaction ->
                when {
                    inputHandler.use(recipe.getMatchingStack(first), transaction).failed -> false
                    else -> outputHandler.take(output, transaction) == HTOutputHandler.TakeResult.FULL
                }
            }

        override fun onComplete(recipe: RECIPE, first: FluidInstance, second: ItemInstance, output: OUTPUT) {
            useTransaction { transaction: Transaction ->
                inputHandler.use(recipe.getMatchingStack(first), transaction)
                outputHandler.take(output, transaction)
                transaction.commit()
            }
            onComplete()
        }
    }
}
