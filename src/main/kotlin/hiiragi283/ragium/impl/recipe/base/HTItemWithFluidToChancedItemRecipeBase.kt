package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.result.HTChancedItemResult
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

abstract class HTItemWithFluidToChancedItemRecipeBase(
    val ingredient: HTItemIngredient,
    val fluidIngredient: HTFluidIngredient,
    final override val results: List<HTChancedItemResult>,
) : HTChancedItemRecipeBase<HTItemWithFluidRecipeInput>(),
    HTItemWithFluidToChancedItemRecipe {
    final override fun isIncompleteIngredient(): Boolean = ingredient.hasNoMatchingStacks() || fluidIngredient.hasNoMatchingStacks()

    final override fun getRequiredCount(stack: ItemStack): Int = ingredient.getRequiredAmount(stack)

    final override fun getRequiredAmount(stack: FluidStack): Int = fluidIngredient.getRequiredAmount(stack)

    final override fun test(input: HTItemWithFluidRecipeInput): Boolean = ingredient.test(input.item) && fluidIngredient.test(input.fluid)
}
