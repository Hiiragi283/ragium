package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.util.HTShapelessRecipeHelper
import hiiragi283.ragium.common.recipe.input.HTChemicalRecipeInput

typealias HTChemicalIngredient = Ior<List<HTItemIngredient>, List<HTFluidIngredient>>
typealias HTChemicalResult = Ior<List<HTItemResult>, List<HTFluidResult>>

abstract class HTChemicalRecipe(val ingredients: HTChemicalIngredient, parameters: SubParameters) :
    HTProcessingRecipe<HTChemicalRecipeInput>(parameters) {
    protected fun matchIngredients(input: HTChemicalRecipeInput): Boolean = ingredients.fold(
        { HTShapelessRecipeHelper.shapelessMatch(it, input.items).isNotEmpty() },
        { HTShapelessRecipeHelper.shapelessMatch(it, input.fluids).isNotEmpty() },
        { items: List<HTItemIngredient>, fluids: List<HTFluidIngredient> ->
            val bool1: Boolean = HTShapelessRecipeHelper.shapelessMatch(items, input.items).isNotEmpty()
            val bool2: Boolean = HTShapelessRecipeHelper.shapelessMatch(fluids, input.fluids).isNotEmpty()
            bool1 && bool2
        },
    )
}
