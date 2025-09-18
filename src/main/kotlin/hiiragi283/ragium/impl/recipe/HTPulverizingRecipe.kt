package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HTPulverizingRecipe(override val ingredient: HTItemIngredient, override val result: HTItemResult) :
    HTItemToItemRecipe,
    HTItemToChancedItemRecipe {
    override fun getIngredientCount(input: SingleRecipeInput): Int = ingredient.getRequiredAmount(input.item())

    override fun getResultItems(input: SingleRecipeInput): List<HTItemToChancedItemRecipe.ChancedResult> =
        listOf(HTItemToChancedItemRecipe.ChancedResult(result, 1f))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PULVERIZING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CRUSHING.get()
}
