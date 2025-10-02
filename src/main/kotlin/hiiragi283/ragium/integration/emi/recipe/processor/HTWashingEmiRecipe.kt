package hiiragi283.ragium.integration.emi.recipe.processor

import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import hiiragi283.ragium.impl.recipe.HTWashingRecipe
import hiiragi283.ragium.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.integration.emi.recipe.base.HTMultiOutputEmiRecipe

class HTWashingEmiRecipe(category: HTEmiRecipeCategory, holder: HTRecipeHolder<HTWashingRecipe>) :
    HTMultiOutputEmiRecipe<HTWashingRecipe>(category, holder) {
    init {
        addInput(recipe.ingredient)
        addInput(recipe.fluidIngredient)

        recipe.results.forEach(::addChancedOutputs)
    }
}
