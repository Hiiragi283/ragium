package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.base.HTBasicChancedRecipe
import hiiragi283.ragium.api.recipe.HTItemAndFluidToChancedRecipe
import java.util.Optional

abstract class HTBasicItemAndFluidToChancedRecipe(
    val itemIngredient: HTItemIngredient,
    val fluidIngredient: HTFluidIngredient,
    result: HTItemResult,
    extraResult: Optional<HTChancedItemResult>,
    time: Int,
) : HTBasicChancedRecipe<HTItemAndFluidRecipeInput>(result, extraResult, time),
    HTItemAndFluidToChancedRecipe {
    final override fun test(input: HTItemAndFluidRecipeInput): Boolean =
        itemIngredient.test(input.item) && fluidIngredient.test(input.fluid)

    final override fun getRequiredFluidAmount(input: HTItemAndFluidRecipeInput): Int = fluidIngredient.amount

    final override fun getRequiredItemAmount(input: HTItemAndFluidRecipeInput): Int = itemIngredient.amount
}
