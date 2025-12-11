package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient

interface HTFluidWithCatalystRecipe :
    HTRecipe,
    HTFluidIngredient.AmountGetter
