package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.multi.HTCombineRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack

abstract class HTBasicCombineRecipe(val itemIngredients: Pair<HTItemIngredient, HTItemIngredient>) : HTCombineRecipe {
    final override fun getLeftRequiredCount(stack: ImmutableItemStack): Int = itemIngredients.first.getRequiredAmount(stack)

    final override fun getRightRequiredCount(stack: ImmutableItemStack): Int = itemIngredients.second.getRequiredAmount(stack)

    final override fun test(left: ImmutableItemStack, right: ImmutableItemStack, fluid: ImmutableFluidStack): Boolean {
        val bool1: Boolean = itemIngredients.first.test(left)
        val bool2: Boolean = itemIngredients.second.test(right)
        val bool3: Boolean = testFluid(fluid)
        return bool1 && bool2 && bool3
    }

    protected abstract fun testFluid(stack: ImmutableFluidStack): Boolean
}
