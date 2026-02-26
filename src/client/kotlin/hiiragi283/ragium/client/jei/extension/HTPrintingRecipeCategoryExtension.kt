package hiiragi283.ragium.client.jei.extension

import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.ragium.api.integration.jei.HTItemAndItemRecipeCategoryExtension
import hiiragi283.ragium.common.recipe.special.HTPrintingRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor

data object HTPrintingRecipeCategoryExtension : HTItemAndItemRecipeCategoryExtension<HTPrintingRecipe> {
    override fun <T : IIngredientAcceptor<T>> setFirstInput(recipe: HTPrintingRecipe, accessor: T) {
        accessor.addItemIngredient(recipe.ingredient)
    }

    override fun <T : IIngredientAcceptor<T>> setSecondInput(recipe: HTPrintingRecipe, accessor: T) {
        accessor.addItemLike(recipe.origin)
    }

    override fun <T : IIngredientAcceptor<T>> setOutput(recipe: HTPrintingRecipe, accessor: T) {
        when (recipe.strategy) {
            HTPrintingRecipe.CopyStrategy.INPUT -> setFirstInput(recipe, accessor)
            HTPrintingRecipe.CopyStrategy.ORIGIN -> setSecondInput(recipe, accessor)
        }
    }
}
