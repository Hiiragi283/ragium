package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.impl.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HTPulverizingRecipe(ingredient: HTItemIngredient, result: HTItemResult) :
    HTItemToItemRecipe(ingredient, result),
    HTItemToChancedItemRecipe {
    override fun getResultItems(input: SingleRecipeInput): List<HTChancedItemRecipe.ChancedResult> =
        listOf(HTChancedItemRecipe.ChancedResult(result))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PULVERIZING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CRUSHING.get()
}
