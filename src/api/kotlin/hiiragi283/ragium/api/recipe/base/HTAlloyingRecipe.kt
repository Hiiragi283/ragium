package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypesNew
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer

class HTAlloyingRecipe(ingredients: List<HTItemIngredient>, result: HTItemResult) :
    HTCombineItemToItemRecipe(RagiumRecipeTypesNew.ALLOYING.get(), ingredients, result) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ALLOYING.get()
}
