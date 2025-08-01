package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypesNew
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer

class HTCrushingRecipe(ingredient: HTItemIngredient, results: List<HTItemResult>, chances: List<Float>) :
    HTItemToChancedItemRecipe(
        RagiumRecipeTypesNew.CRUSHING.get(),
        ingredient,
        results,
        chances,
    ) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CRUSHING.get()
}
