package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.recipe.base.HTLocationToRecipe
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.transfer.useTransaction
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class HTLocationToBlockEntity<OUTPUT : Any, RECIPE : HTLocationToRecipe<OUTPUT>>(
    type: BlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTDoubleProcessorBlockEntity<ServerLevel, BlockPos, OUTPUT, RECIPE>(type, pos, state) {

    //    HTDoubleProcessorBlockEntity    //

    final override val recipeCache: HTRecipeCacheHelper.Located<RECIPE> = HTRecipeCacheHelper.Located(getLookup())

    protected abstract fun getLookup(): HTRecipeLookup<RECIPE>

    protected val outputHandler: HTOutputSlot<OUTPUT> = createOutputHandler()

    protected abstract fun createOutputHandler(): HTOutputSlot<OUTPUT>

    final override fun getFirstInput(): ServerLevel = level as? ServerLevel ?: error("Cannot process recipe on client-side")

    final override fun getSecondInput(): BlockPos = blockPos

    override fun canComplete(recipe: RECIPE, first: ServerLevel, second: BlockPos, output: OUTPUT): Boolean =
        useTransaction { outputHandler.take(output, it) == HTOutputSlot.TakeResult.FULL }

    override fun getMaxRecipeProgress(recipe: RECIPE, first: ServerLevel, second: BlockPos): Int =
        getMaxRecipeProgress(recipe.getProgressData(first, second).time)

    override fun onComplete(recipe: RECIPE, first: ServerLevel, second: BlockPos, output: OUTPUT) {
        useTransaction {
            outputHandler.take(output, it)
            it.commit()
        }
        onComplete()
    }

    protected open fun onComplete() {}
}
