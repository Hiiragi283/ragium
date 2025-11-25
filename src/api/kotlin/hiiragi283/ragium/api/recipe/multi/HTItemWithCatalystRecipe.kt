package hiiragi283.ragium.api.recipe.multi

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import java.util.Optional

interface HTItemWithCatalystRecipe :
    HTMultiInputsToObjRecipe,
    HTItemIngredient.CountGetter {
    val required: HTItemIngredient
    val optional: Optional<HTItemIngredient>
}
