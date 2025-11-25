package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup

/**
 * [HTComplexRecipe]の抽象クラス
 */
abstract class HTBasicComplexRecipe(
    val itemIngredients: List<HTItemIngredient>,
    val fluidIngredients: List<HTFluidIngredient>,
    val results: HTComplexResult,
) : HTComplexRecipe {
    final override fun getRequiredCount(index: Int, stack: ImmutableItemStack): Int = itemIngredients[index].getRequiredAmount(stack)

    final override fun getRequiredAmount(index: Int, stack: ImmutableFluidStack): Int = fluidIngredients[index].getRequiredAmount(stack)

    final override fun test(input: HTMultiRecipeInput): Boolean {
        val bool1: Boolean = HTMultiRecipeInput.hasMatchingSlots(itemIngredients, input.items)
        val bool2: Boolean = HTMultiRecipeInput.hasMatchingSlots(fluidIngredients, input.fluids)
        return bool1 && bool2
    }

    final override fun assembleItem(input: HTMultiRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, provider, results.getLeft())

    final override fun isIncomplete(): Boolean {
        val bool1: Boolean = itemIngredients.isEmpty() || itemIngredients.any(HTItemIngredient::hasNoMatchingStacks)
        val bool2: Boolean = fluidIngredients.isEmpty() || fluidIngredients.any(HTFluidIngredient::hasNoMatchingStacks)
        val bool3: Boolean = results.map(HTItemResult::hasNoMatchingStack, HTFluidResult::hasNoMatchingStack)
        return bool1 || bool2 || bool3
    }

    final override fun assembleFluid(input: HTMultiRecipeInput, provider: HolderLookup.Provider): ImmutableFluidStack? =
        getFluidResult(input, provider, results.getRight())
}
