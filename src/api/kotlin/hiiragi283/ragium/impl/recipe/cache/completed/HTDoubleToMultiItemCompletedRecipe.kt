package hiiragi283.ragium.impl.recipe.cache.completed

import hiiragi283.core.api.recipe.HTBiRecipeFactory
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.api.recipe.input.HTItemListRecipeInput
import hiiragi283.core.impl.recipe.cache.completed.HTCompletedRecipe
import hiiragi283.ragium.api.recipe.base.HTPlantingRecipe
import net.minecraft.world.item.ItemStack

/**
 * @see hiiragi283.core.impl.recipe.cache.completed.HTSingleToMultiItemCompletedRecipe
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
    private val amountGetter: (RECIPE, INPUT_A, INPUT_B) -> Pair<Int, Int>,
) : HTCompletedRecipe.WithProgress<RECIPE>(recipe) {
    private val output: Iterable<ItemStack> = recipe.assemble(firstInputHandler.getStack(), secondInputHandler.getStack())

    override fun canComplete(): Boolean = output.all(outputHandler::canInsert)

    override fun complete() {
        output.forEach(outputHandler::insert)
        amountGetter(recipe, firstInputHandler.getStack(), secondInputHandler.getStack()).let { (first: Int, second: Int) ->
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
            HTPlantingRecipe::getRequiredAmount,
        ) {
        override fun getProgress(): HTProgressData = HTItemListRecipeInput(firstInputHandler.getStack(), secondInputHandler.getStack())
            .let(recipe::getProgressData)
    }
}
