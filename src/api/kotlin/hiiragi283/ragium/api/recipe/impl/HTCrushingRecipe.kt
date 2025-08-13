package hiiragi283.ragium.api.recipe.impl

import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipeBase
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTCrushingRecipe(ingredient: HTItemIngredient, results: List<HTItemResult>, chances: List<Float>) :
    HTItemToChancedItemRecipeBase(ingredient, results, chances) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CRUSHING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CRUSHING.get()
}
