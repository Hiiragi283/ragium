package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.recipe.chance.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.impl.recipe.base.HTBasicSingleOutputRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class HTPulverizingRecipe(val ingredient: HTItemIngredient, result: HTItemResult) :
    HTBasicSingleOutputRecipe<SingleRecipeInput>(result),
    HTItemToChancedItemRecipe {
    override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    override fun isIncomplete(): Boolean = ingredient.hasNoMatchingStacks() || result.hasNoMatchingStack()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PULVERIZING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.CRUSHING.get()

    override fun getResultItems(input: SingleRecipeInput): List<HTItemResultWithChance> = listOf(HTItemResultWithChance(result))

    override fun getRequiredCount(stack: ImmutableItemStack): Int = ingredient.getRequiredAmount(stack)
}
