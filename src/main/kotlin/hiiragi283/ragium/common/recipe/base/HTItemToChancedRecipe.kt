package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import java.util.Optional

abstract class HTItemToChancedRecipe(
    val ingredient: HTItemIngredient,
    result: HTItemResult,
    extraResult: Optional<HTChancedItemResult>,
    time: Int,
) : HTChancedRecipe<SingleRecipeInput>(result, extraResult, time) {
    final override fun matches(input: SingleRecipeInput, level: Level): Boolean = ingredient.test(input.item())
}
