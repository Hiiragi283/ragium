package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTCombineRecipe
import hiiragi283.ragium.api.stack.ImmutableItemStack

abstract class HTBasicCombineRecipe(val itemIngredients: Pair<HTItemIngredient, HTItemIngredient>) : HTCombineRecipe {
    final override fun getLeftRequiredCount(stack: ImmutableItemStack): Int = itemIngredients.first.getRequiredAmount(stack)

    final override fun getRightRequiredCount(stack: ImmutableItemStack): Int = itemIngredients.second.getRequiredAmount(stack)

    final override fun test(input: HTMultiRecipeInput): Boolean {
        val bool1: Boolean = itemIngredients.first.test(input.getItem(0))
        val bool2: Boolean = itemIngredients.second.test(input.getItem(1))
        val bool3: Boolean = testFluid(input)
        return bool1 && bool2 && bool3
    }

    protected abstract fun testFluid(input: HTMultiRecipeInput): Boolean
}
