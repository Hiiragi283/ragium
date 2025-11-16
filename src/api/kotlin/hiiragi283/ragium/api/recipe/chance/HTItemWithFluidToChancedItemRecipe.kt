package hiiragi283.ragium.api.recipe.chance

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput

/**
 * 単一のアイテムと液体から複数の確率付きの完成品を生産するレシピ
 */
interface HTItemWithFluidToChancedItemRecipe :
    HTChancedItemRecipe<HTItemWithFluidRecipeInput>,
    HTItemIngredient.CountGetter,
    HTFluidIngredient.AmountGetter
