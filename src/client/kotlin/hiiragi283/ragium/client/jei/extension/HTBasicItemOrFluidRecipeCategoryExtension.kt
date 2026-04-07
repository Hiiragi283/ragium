package hiiragi283.ragium.client.jei.extension

import hiiragi283.core.api.integration.jei.addFluidIngredient
import hiiragi283.core.api.integration.jei.addFluidResult
import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.ragium.impl.recipe.HTBasicItemOrFluidRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor

class HTBasicItemOrFluidRecipeCategoryExtension<RECIPE : HTBasicItemOrFluidRecipe> : HTItemOrFluidRecipeCategoryExtension<RECIPE> {
    override fun <T : IIngredientAcceptor<T>> setInputFluid(recipe: RECIPE, accessor: T) {
        accessor.addFluidIngredient(recipe.ingredient.getRight())
    }

    override fun <T : IIngredientAcceptor<T>> setInputItem(recipe: RECIPE, accessor: T) {
        accessor.addItemIngredient(recipe.ingredient.getLeft())
    }

    override fun <T : IIngredientAcceptor<T>> setOutputItem(recipe: RECIPE, accessor: T) {
        accessor.addItemResult(recipe.result.getLeft())
    }

    override fun <T : IIngredientAcceptor<T>> setOutputFluid(recipe: RECIPE, accessor: T) {
        accessor.addFluidResult(recipe.result.getRight())
    }
}
