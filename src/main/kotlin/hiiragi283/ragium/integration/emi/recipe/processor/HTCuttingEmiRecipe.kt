package hiiragi283.ragium.integration.emi.recipe.processor

import dev.emi.emi.api.stack.EmiIngredient
import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import hiiragi283.ragium.impl.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.integration.emi.recipe.base.HTMultiOutputEmiRecipe

class HTCuttingEmiRecipe(category: HTEmiRecipeCategory, holder: HTRecipeHolder<HTItemToItemRecipe>) :
    HTMultiOutputEmiRecipe<HTItemToItemRecipe>(category, holder) {
    init {
        addInput(recipe.ingredient)

        addOutputs(recipe.result)
    }

    override fun getCatalystStack(): EmiIngredient = output(0).copy().setAmount(1)

    override fun shouldCatalyst(): Boolean = true
}
