package hiiragi283.ragium.client.emi.recipe

import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.common.recipe.base.HTChemicalIngredient
import hiiragi283.ragium.common.recipe.base.HTChemicalRecipe
import net.minecraft.world.item.crafting.RecipeHolder

abstract class HTChemicalEmiRecipe<RECIPE : HTChemicalRecipe>(
    maxItem: Int,
    maxFluid: Int,
    category: HTEmiRecipeCategory,
    holder: RecipeHolder<RECIPE>,
) : HTProcessingEmiRecipe<RECIPE>(category, holder) {
    init {
        val ingredients: HTChemicalIngredient = recipe.ingredients
        val items: List<HTItemIngredient> = ingredients.getLeft() ?: listOf()
        for (i: Int in (0..<maxItem)) {
            addInput(items.getOrNull(i))
        }
        val fluids: List<HTFluidIngredient> = ingredients.getRight() ?: listOf()
        for (i: Int in (0..<maxFluid)) {
            addInput(fluids.getOrNull(i))
        }

        addOutputs()
    }

    protected abstract fun addOutputs()
}
