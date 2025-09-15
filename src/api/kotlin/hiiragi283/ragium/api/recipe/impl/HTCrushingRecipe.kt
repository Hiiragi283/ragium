package hiiragi283.ragium.api.recipe.impl

import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipeBase
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTCrushingRecipe(ingredient: HTItemIngredient, results: List<HTItemToChancedItemRecipe.ChancedResult>) :
    HTItemToChancedItemRecipeBase(ingredient, results) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.INSTANCE.crushing

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CRUSHING.get()
}
