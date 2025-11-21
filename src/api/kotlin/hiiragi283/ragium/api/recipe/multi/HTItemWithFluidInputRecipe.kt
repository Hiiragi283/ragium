package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput

/**
 * 一種類のアイテムと液体から完成品を作成するレシピ
 */
interface HTItemWithFluidInputRecipe :
    HTRecipe<HTItemWithFluidRecipeInput>,
    HTItemIngredient.CountGetter,
    HTFluidIngredient.AmountGetter
