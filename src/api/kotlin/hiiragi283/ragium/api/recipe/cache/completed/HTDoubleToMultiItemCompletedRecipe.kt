package hiiragi283.ragium.api.recipe.cache.completed

import hiiragi283.core.api.recipe.HTBiRecipeFactory
import hiiragi283.core.api.recipe.cache.completed.HTCompletedRecipe
import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.api.recipe.progress.HTProgressData
import hiiragi283.ragium.api.recipe.base.HTPlantingRecipe
import net.minecraft.world.item.ItemStack

/**
 * @see hiiragi283.core.api.recipe.cache.completed.HTSingleToMultiItemCompletedRecipe
 */
abstract class HTDoubleToMultiItemCompletedRecipe<
    INPUT_A : Any,
    INPUT_B : Any,
    RECIPE : HTBiRecipeFactory<INPUT_A, INPUT_B, out Iterable<ItemStack>>,
    >(
    recipe: RECIPE,
    protected val firstInputHandler: HTInputHandler<INPUT_A>,
    protected val secondInputHandler: HTInputHandler<INPUT_B>,
    protected val outputHandler: HTOutputHandler<ItemStack>,
    private val amountGetter: (RECIPE, INPUT_A, INPUT_B) -> Pair<INPUT_A, INPUT_B>,
) : HTCompletedRecipe.WithProgress<RECIPE>(recipe) {
    private val output: Iterable<ItemStack> = recipe.assemble(firstInputHandler.getStack(), secondInputHandler.getStack())

    override fun canComplete(): Boolean = output.all(outputHandler::canInsert)

    override fun complete() {
        output.forEach(outputHandler::insert)
        amountGetter(recipe, firstInputHandler.getStack(), secondInputHandler.getStack()).let { (first: INPUT_A, second: INPUT_B) ->
            firstInputHandler.consume(first)
            secondInputHandler.consume(second)
        }
    }

    class Planting(
        recipe: HTPlantingRecipe,
        firstInputHandler: HTInputHandler<ItemStack>,
        secondInputHandler: HTInputHandler<ItemStack>,
        outputHandler: HTOutputHandler<ItemStack>,
    ) : HTDoubleToMultiItemCompletedRecipe<ItemStack, ItemStack, HTPlantingRecipe>(
        recipe,
        firstInputHandler,
        secondInputHandler,
        outputHandler,
        HTPlantingRecipe::getMatchingStacks,
    ) {
        override fun getProgress(): HTProgressData = recipe.getProgressData(firstInputHandler.getStack(), secondInputHandler.getStack())
    }
}
