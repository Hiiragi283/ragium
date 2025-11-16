package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient

/**
 * 複数のアイテムから完成品を生産するレシピ
 */
interface HTMultiItemsToItemRecipe : HTMultiInputsToObjRecipe {
    val ingredients: List<HTItemIngredient>
}
