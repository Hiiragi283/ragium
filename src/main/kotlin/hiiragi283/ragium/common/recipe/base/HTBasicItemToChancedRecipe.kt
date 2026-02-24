package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.HTItemToChancedRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput
import java.util.Optional

abstract class HTBasicItemToChancedRecipe(
    val ingredient: HTItemIngredient,
    result: HTItemResult,
    extraResult: Optional<HTChancedItemResult>,
    time: Int,
) : HTBasicChancedRecipe<SingleRecipeInput>(result, extraResult, time),
    HTItemToChancedRecipe.Serializable {
    final override fun getRequiredAmount(input: SingleRecipeInput): Int = ingredient.amount

    final override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())
}
