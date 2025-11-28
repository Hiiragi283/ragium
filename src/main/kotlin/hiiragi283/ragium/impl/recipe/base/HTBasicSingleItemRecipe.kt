package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.single.HTSingleItemRecipe
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

abstract class HTBasicSingleItemRecipe(val ingredient: HTItemIngredient) : HTSingleItemRecipe {
    final override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    final override fun isIncomplete(): Boolean = ingredient.hasNoMatchingStacks() || isIncompleteResult()

    protected abstract fun isIncompleteResult(): Boolean

    final override fun getRequiredCount(stack: ImmutableItemStack): Int = ingredient.getRequiredAmount(stack)
}
