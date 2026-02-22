package hiiragi283.ragium.client.jei.extension

import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.ragium.api.integration.jei.HTItemAndItemRecipeCategoryExtension
import hiiragi283.ragium.common.recipe.base.HTBasicItemAndItemRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor

class HTBasicItemAndItemRecipeCategoryExtension<RECIPE : HTBasicItemAndItemRecipe> : HTItemAndItemRecipeCategoryExtension<RECIPE> {
    override fun <T : IIngredientAcceptor<T>> setFirstInput(recipe: RECIPE, accessor: T) {
        accessor.addItemIngredient(recipe.first)
    }

    override fun <T : IIngredientAcceptor<T>> setSecondInput(recipe: RECIPE, accessor: T) {
        accessor.addItemIngredient(recipe.second)
    }

    override fun <T : IIngredientAcceptor<T>> setOutput(recipe: RECIPE, accessor: T) {
        accessor.addItemResult(recipe.result)
    }
}
