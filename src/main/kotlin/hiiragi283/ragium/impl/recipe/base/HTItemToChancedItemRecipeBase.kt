package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTChancedItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

abstract class HTItemToChancedItemRecipeBase(val ingredient: HTItemIngredient, override val results: List<HTChancedItemResult>) :
    HTChancedItemRecipeBase<SingleRecipeInput>(),
    HTItemToChancedItemRecipe {
    final override fun getRequiredCount(stack: ImmutableItemStack): Int = ingredient.getRequiredAmount(stack)

    final override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    final override fun isIncompleteIngredient(): Boolean = ingredient.hasNoMatchingStacks()
}
