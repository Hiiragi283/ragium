package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.HTFluidRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTDoubleRecipeInput

/**
 * 単一のアイテムと触媒から複数の完成品（アイテム，液体）を生産するレシピ
 */
interface HTItemWithCatalystRecipe :
    HTFluidRecipe<HTDoubleRecipeInput>,
    HTItemIngredient.CountGetter
