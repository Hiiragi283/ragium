package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.recipe.chance.HTItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack

/**
 * [HTItemWithFluidToChancedItemRecipe]の抽象クラス
 */
abstract class HTItemWithFluidToChancedItemRecipeBase(
    val ingredient: HTItemIngredient,
    val fluidIngredient: HTFluidIngredient,
    final override val results: List<HTItemResultWithChance>,
) : HTChancedItemRecipeBase<HTItemWithFluidRecipeInput>(),
    HTItemWithFluidToChancedItemRecipe {
    final override fun isIncompleteIngredient(): Boolean = ingredient.hasNoMatchingStacks() || fluidIngredient.hasNoMatchingStacks()

    final override fun getRequiredCount(stack: ImmutableItemStack): Int = ingredient.getRequiredAmount(stack)

    final override fun getRequiredAmount(stack: ImmutableFluidStack): Int = fluidIngredient.getRequiredAmount(stack)

    final override fun test(input: HTItemWithFluidRecipeInput): Boolean = ingredient.test(input.item) && fluidIngredient.test(input.fluid)
}
