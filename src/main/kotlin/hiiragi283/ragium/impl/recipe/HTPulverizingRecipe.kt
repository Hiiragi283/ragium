package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.recipe.chance.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.impl.recipe.base.HTBasicSingleItemRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HTPulverizingRecipe(ingredient: HTItemIngredient, result: HTItemResult) :
    HTBasicSingleItemRecipe(ingredient, result),
    HTItemToChancedItemRecipe {
    override fun getResultItems(input: SingleRecipeInput): List<HTItemResultWithChance> = listOf(HTItemResultWithChance(result))

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PULVERIZING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CRUSHING.get()
}
