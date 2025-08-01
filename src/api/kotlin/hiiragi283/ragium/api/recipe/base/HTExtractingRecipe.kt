package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTItemToItemRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypesNew
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer

class HTExtractingRecipe(ingredient: HTItemIngredient, result: HTItemResult) :
    HTItemToItemRecipe(RagiumRecipeTypesNew.EXTRACTING.get(), ingredient, result) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.EXTRACTING.get()
}
