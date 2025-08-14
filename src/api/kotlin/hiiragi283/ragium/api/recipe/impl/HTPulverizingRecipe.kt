package hiiragi283.ragium.api.recipe.impl

import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeSerializers
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HTPulverizingRecipe(ingredient: HTItemIngredient, result: HTItemResult) :
    HTItemToItemRecipe(ingredient, result),
    HTItemToChancedItemRecipe {
    override fun getIngredientCount(input: SingleRecipeInput): Int = ingredient.getRequiredAmount(input.item())

    override fun getResultItems(input: SingleRecipeInput): List<Pair<ItemStack, Float>> {
        val stack: ItemStack = result.getOrEmpty()
        return if (stack.isEmpty) listOf() else listOf(stack to 1f)
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PULVERIZING.get()

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CRUSHING.get()
}
