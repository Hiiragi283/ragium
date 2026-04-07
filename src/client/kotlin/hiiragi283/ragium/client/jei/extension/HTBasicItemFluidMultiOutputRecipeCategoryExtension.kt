package hiiragi283.ragium.client.jei.extension

import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.impl.recipe.HTBasicItemFluidMultiOutputRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor

class HTBasicItemFluidMultiOutputRecipeCategoryExtension<RECIPE : HTBasicItemFluidMultiOutputRecipe> :
    HTItemFluidMultiOutputRecipeCategoryExtension<RECIPE> {
    override fun <T : IIngredientAcceptor<T>> setItemInput(recipe: RECIPE, accessor: T) {
        accessor.addItemIngredient(recipe.itemIngredient)
    }

    override fun <T : IIngredientAcceptor<T>> setFluidInput(recipe: RECIPE, accessor: T) {
        accessor.addFluidIngredient(recipe.fluidIngredient)
    }

    override fun <T : IIngredientAcceptor<T>> setOutput(recipe: RECIPE, index: Int, accessor: T) {
        val result: HTItemResult = recipe.results.getOrNull(index) ?: return
        accessor.addItemResult(result)
    }
}
