package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTItemWithCatalystRecipe
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import java.util.Optional

/**
 * [HTItemWithCatalystRecipe]の抽象クラス
 */
abstract class HTBasicItemWithCatalystRecipe(
    val required: HTItemIngredient,
    val optional: Optional<HTItemIngredient>,
    results: HTComplexResult,
) : HTBasicComplexOutputRecipe<HTMultiRecipeInput>(results),
    HTItemWithCatalystRecipe {
    final override fun isIncompleteIngredient(): Boolean {
        val bool1: Boolean = required.hasNoMatchingStacks()
        val bool2: Boolean = optional.map(HTItemIngredient::hasNoMatchingStacks).orElse(false)
        return bool1 || bool2
    }
}
