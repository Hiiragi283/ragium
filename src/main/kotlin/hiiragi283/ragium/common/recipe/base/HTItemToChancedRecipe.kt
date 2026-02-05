package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level

abstract class HTItemToChancedRecipe(
    val ingredient: HTItemIngredient,
    result: HTItemResult,
    extraResults: List<HTChancedItemResult>,
    parameters: SubParameters,
) : HTChancedRecipe<SingleRecipeInput>(result, extraResults, parameters) {
    final override fun matches(input: SingleRecipeInput, level: Level): Boolean = ingredient.test(input.item())
}
