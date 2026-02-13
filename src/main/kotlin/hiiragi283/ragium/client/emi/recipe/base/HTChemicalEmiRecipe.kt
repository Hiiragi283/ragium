package hiiragi283.ragium.client.emi.recipe.base

import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.common.recipe.base.HTChemicalIngredient
import hiiragi283.ragium.common.recipe.base.HTChemicalRecipe
import hiiragi283.ragium.common.recipe.base.HTChemicalResult
import net.minecraft.world.item.crafting.RecipeHolder

abstract class HTChemicalEmiRecipe<RECIPE : HTChemicalRecipe>(
    maxItemIn: Int,
    maxFluidIn: Int,
    maxItemOut: Int,
    maxFluidOut: Int,
    category: HTEmiRecipeCategory,
    holder: RecipeHolder<RECIPE>,
) : HTProcessingEmiRecipe<RECIPE>(category, holder) {
    init {
        val ingredients: HTChemicalIngredient = recipe.ingredients
        val itemsIngredients: List<HTItemIngredient> = ingredients.getLeft() ?: listOf()
        for (i: Int in (0..<maxItemIn)) {
            addInput(itemsIngredients.getOrNull(i))
        }
        val fluidIngredients: List<HTFluidIngredient> = ingredients.getRight() ?: listOf()
        for (i: Int in (0..<maxFluidIn)) {
            addInput(fluidIngredients.getOrNull(i))
        }

        val results: HTChemicalResult = recipe.results
        val itemResults: List<HTItemResult> = results.getLeft() ?: listOf()
        for (i: Int in (0..<maxItemOut)) {
            addOutputs(itemResults.getOrNull(i))
        }
        val fluidResults: List<HTFluidResult> = results.getRight() ?: listOf()
        for (i: Int in (0..<maxFluidOut)) {
            addOutputs(fluidResults.getOrNull(i))
        }
    }
}
