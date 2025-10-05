package hiiragi283.ragium.integration.emi.recipe.processor

import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import hiiragi283.ragium.impl.recipe.base.HTItemWithFluidToChancedItemRecipeBase
import hiiragi283.ragium.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.integration.emi.recipe.base.HTMultiOutputEmiRecipe

class HTItemWithFluidToChancedItemEmiRecipe(category: HTEmiRecipeCategory, holder: HTRecipeHolder<HTItemWithFluidToChancedItemRecipeBase>) :
    HTMultiOutputEmiRecipe<HTItemWithFluidToChancedItemRecipeBase>(category, holder) {
    init {
        addInput(recipe.ingredient)
        addInput(recipe.fluidIngredient)

        recipe.results.forEach(::addChancedOutputs)
    }
}
