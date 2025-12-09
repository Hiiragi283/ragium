package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput

/**
 * 複数のアイテムから完成品を生産するレシピ
 *
 * 材料の並びは不定
 */
interface HTShapelessInputsRecipe : HTRecipe<HTMultiRecipeInput> {
    val ingredients: List<HTItemIngredient>
}
