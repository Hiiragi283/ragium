package hiiragi283.ragium.client.jei.extension

import hiiragi283.core.api.integration.jei.addItemIngredient
import hiiragi283.core.api.integration.jei.addItemResult
import hiiragi283.ragium.api.integration.jei.HTItemToChancedRecipeCategoryExtension
import hiiragi283.ragium.common.recipe.base.HTBasicItemToChancedRecipe
import mezz.jei.api.gui.builder.IIngredientAcceptor
import kotlin.jvm.optionals.getOrNull

class HTBasicItemToChancedRecipeCategoryExtension<RECIPE : HTBasicItemToChancedRecipe> : HTItemToChancedRecipeCategoryExtension<RECIPE> {
    override fun <T : IIngredientAcceptor<T>> setInput(recipe: RECIPE, accessor: T) {
        accessor.addItemIngredient(recipe.ingredient)
    }

    override fun <T : IIngredientAcceptor<T>> setOutput(recipe: RECIPE, accessor: T) {
        accessor.addItemResult(recipe.result)
    }

    override fun <T : IIngredientAcceptor<T>> setExtraOutput(recipe: RECIPE, accessor: T) {
        accessor.addItemResult(recipe.extraResult.getOrNull())
    }
}
