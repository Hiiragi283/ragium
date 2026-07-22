package hiiragi283.ragium.impl.recipe.cache.completed

import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.cache.completed.HTCompletedRecipe
import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.api.recipe.input.HTItemListRecipeInput
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import net.minecraft.world.item.ItemStack

class HTAlloyingCompletedRecipe(
    recipe: HTAlloyingRecipe,
    private val primaryInputHandler: HTInputHandler<ItemStack>,
    private val secondaryInputHandler: HTInputHandler<ItemStack>,
    private val tertiaryInputHandler: HTInputHandler<ItemStack>,
    private val outputHandler: HTOutputHandler<ItemStack>,
) : HTCompletedRecipe.WithProgress<HTAlloyingRecipe>(recipe) {
    private val output: ItemStack = recipe.assemble(
        primaryInputHandler.getStack(),
        secondaryInputHandler.getStack(),
        tertiaryInputHandler.getStack(),
    )

    override fun getProgress(): HTProgressData = listOf(primaryInputHandler, secondaryInputHandler, tertiaryInputHandler)
        .map(HTInputHandler<ItemStack>::getStack)
        .let(::HTItemListRecipeInput)
        .let(recipe::getProgressData)

    override fun canComplete(): Boolean = outputHandler.canInsert(output)

    override fun complete() {
        // output
        outputHandler.insert(output)
        recipe
            .getMatchingStacks(primaryInputHandler.getStack(), secondaryInputHandler.getStack(), tertiaryInputHandler.getStack())
            .let { (first: ItemStack, second: ItemStack, third: ItemStack) ->
                primaryInputHandler.consume(first)
                secondaryInputHandler.consume(second)
                tertiaryInputHandler.consume(third)
            }
    }
}
