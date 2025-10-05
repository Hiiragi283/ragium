package hiiragi283.ragium.integration.emi.recipe.processor

import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.manager.HTRecipeHolder
import hiiragi283.ragium.impl.recipe.HTCrushingRecipe
import hiiragi283.ragium.impl.recipe.HTPulverizingRecipe
import hiiragi283.ragium.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.integration.emi.recipe.base.HTMultiOutputEmiRecipe
import hiiragi283.ragium.integration.emi.toTagEmi
import hiiragi283.ragium.setup.RagiumFluidContents

class HTCrushingEmiRecipe(category: HTEmiRecipeCategory, holder: HTRecipeHolder<HTItemToChancedItemRecipe>) :
    HTMultiOutputEmiRecipe<HTItemToChancedItemRecipe>(category, holder) {
    init {
        if (recipe is HTCrushingRecipe) {
            addInput(recipe.ingredient)

            recipe.results.forEach(::addChancedOutputs)
        } else if (recipe is HTPulverizingRecipe) {
            addInput(recipe.ingredient)

            addOutputs(recipe.result)
        }
        addInput(RagiumFluidContents.LUBRICANT.toTagEmi())
    }

    override fun shouldCatalyst(): Boolean = true
}
