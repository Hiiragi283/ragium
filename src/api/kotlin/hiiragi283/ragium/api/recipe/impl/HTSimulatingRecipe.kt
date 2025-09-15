package hiiragi283.ragium.api.recipe.impl

import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.*

class HTSimulatingRecipe(ingredient: Optional<HTItemIngredient>, catalyst: HTItemIngredient, result: HTItemResult) :
    HTItemWithCatalystToItemRecipe(ingredient, catalyst, result) {
    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.INSTANCE.simulating

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.SIMULATING.get()
}
