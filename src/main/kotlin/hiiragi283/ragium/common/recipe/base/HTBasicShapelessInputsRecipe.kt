package hiiragi283.ragium.common.recipe.base

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTShapelessInputsRecipe
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.level.Level

abstract class HTBasicShapelessInputsRecipe(final override val ingredients: List<HTItemIngredient>, result: HTItemResult) :
    HTBasicSingleOutputRecipe(result),
    HTShapelessInputsRecipe {
    final override fun matches(input: HTRecipeInput, level: Level): Boolean = HTRecipeInput.hasMatchingSlots(ingredients, input.items)
}
