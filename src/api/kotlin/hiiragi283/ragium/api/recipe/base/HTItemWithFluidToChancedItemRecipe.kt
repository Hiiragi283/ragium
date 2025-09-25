package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput

/**
 * アイテムと液体の両方を受け付ける[HTChancedItemRecipe]の拡張インターフェース
 */
interface HTItemWithFluidToChancedItemRecipe :
    HTChancedItemRecipe<HTItemWithFluidRecipeInput>,
    HTFluidIngredient.CountGetter
