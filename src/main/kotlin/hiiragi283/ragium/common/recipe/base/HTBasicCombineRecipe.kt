package hiiragi283.ragium.common.recipe.base

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.multi.HTCombineRecipe
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack

abstract class HTBasicCombineRecipe(val itemIngredients: Pair<HTItemIngredient, HTItemIngredient>) : HTCombineRecipe {
    constructor(left: HTItemIngredient, right: HTItemIngredient) : this(left to right)

    final override fun getLeftRequiredCount(): Int = itemIngredients.first.getRequiredAmount()

    final override fun getRightRequiredCount(): Int = itemIngredients.second.getRequiredAmount()

    final override fun test(left: ImmutableItemStack, right: ImmutableItemStack, fluid: ImmutableFluidStack): Boolean {
        val bool1: Boolean = itemIngredients.first.test(left)
        val bool2: Boolean = itemIngredients.second.test(right)
        val bool3: Boolean = testFluid(fluid)
        return bool1 && bool2 && bool3
    }

    protected abstract fun testFluid(stack: ImmutableFluidStack): Boolean
}
