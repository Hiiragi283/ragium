package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult

abstract class HTCombineItemToItemRecipe(ingredients: List<HTItemIngredient>, result: HTItemResult) :
    HTCombineItemToObjRecipe<HTItemResult>(ingredients, result)
