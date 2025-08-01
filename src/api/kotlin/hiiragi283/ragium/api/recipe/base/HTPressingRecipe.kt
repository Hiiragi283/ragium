package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import java.util.*

class HTPressingRecipe(ingredient: HTItemIngredient, catalyst: Optional<HTItemIngredient>, result: HTItemResult) :
    HTItemWithCatalystToItemRecipe(RagiumRecipeTypes.PRESSING.get(), ingredient, catalyst, result) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PRESSING.get()
}
