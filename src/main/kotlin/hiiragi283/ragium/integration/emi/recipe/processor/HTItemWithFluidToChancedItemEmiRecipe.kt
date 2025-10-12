package hiiragi283.ragium.integration.emi.recipe.processor

import hiiragi283.ragium.impl.recipe.base.HTItemWithFluidToChancedItemRecipeBase
import hiiragi283.ragium.integration.emi.HTEmiRecipeCategory
import hiiragi283.ragium.integration.emi.recipe.base.HTMultiOutputEmiRecipe
import net.minecraft.world.item.crafting.RecipeHolder

class HTItemWithFluidToChancedItemEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<HTItemWithFluidToChancedItemRecipeBase>) :
    HTMultiOutputEmiRecipe<HTItemWithFluidToChancedItemRecipeBase>(category, holder) {
    init {
        addInput(recipe.ingredient)
        addInput(recipe.fluidIngredient)

        recipe.results.forEach(::addChancedOutputs)
    }
}
