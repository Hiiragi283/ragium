package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.fluid.HTFluidRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient

/**
 * 単一のアイテムと触媒から単一のアイテムを生産するレシピ
 */
interface HTItemWithCatalystRecipe :
    HTFluidRecipe,
    HTItemIngredient.CountGetter
