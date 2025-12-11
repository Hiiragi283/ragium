package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient

/**
 * 単一の液体と触媒から複数の完成品（アイテム，液体）を生産するレシピ
 */
interface HTFluidWithCatalystRecipe :
    HTRecipe,
    HTFluidIngredient.AmountGetter
