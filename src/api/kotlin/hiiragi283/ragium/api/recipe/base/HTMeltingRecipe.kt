package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import net.minecraft.world.item.crafting.RecipeSerializer

class HTMeltingRecipe(itemIngredient: HTItemIngredient, result: HTFluidResult) :
    HTItemToFluidRecipe(RagiumRecipeTypes.MELTING.get(), itemIngredient, result) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MELTING.get()
}
