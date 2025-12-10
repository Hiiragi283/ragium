package hiiragi283.ragium.client.integration.emi.recipe.processor

import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.recipe.base.HTCombineEmiRecipe
import hiiragi283.ragium.client.integration.emi.toEmi
import hiiragi283.ragium.common.recipe.HTBrewingRecipe
import net.minecraft.resources.ResourceLocation

class HTBrewingEmiRecipe(id: ResourceLocation, recipe: HTBrewingRecipe) :
    HTCombineEmiRecipe<HTBrewingRecipe>(RagiumEmiRecipeCategories.BREWING, id, recipe) {
    init {
        addInput(recipe.left.toEmi())
        addInput(recipe.ingredient)
        addInput(HTBrewingRecipe.FLUID_INGREDIENT)

        addOutputs(recipe.potionDrop.toEmi())
    }
}
