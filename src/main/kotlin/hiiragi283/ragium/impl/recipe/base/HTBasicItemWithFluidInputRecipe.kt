package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTItemWithFluidInputRecipe
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack

abstract class HTBasicItemWithFluidInputRecipe(
    val itemIngredient: HTItemIngredient,
    val fluidIngredient: HTFluidIngredient,
    result: HTItemResult,
) : HTBasicSingleOutputRecipe<HTItemWithFluidRecipeInput>(result),
    HTItemWithFluidInputRecipe {
    final override fun test(input: HTItemWithFluidRecipeInput): Boolean =
        itemIngredient.test(input.item) && fluidIngredient.test(input.fluid)

    final override fun isIncomplete(): Boolean {
        val bool1: Boolean = itemIngredient.hasNoMatchingStacks()
        val bool2: Boolean = fluidIngredient.hasNoMatchingStacks()
        val bool3: Boolean = result.hasNoMatchingStack()
        return bool1 || bool2 || bool3
    }

    final override fun getRequiredCount(stack: ImmutableItemStack): Int = itemIngredient.getRequiredAmount(stack)

    final override fun getRequiredAmount(stack: ImmutableFluidStack): Int = fluidIngredient.getRequiredAmount(stack)
}
