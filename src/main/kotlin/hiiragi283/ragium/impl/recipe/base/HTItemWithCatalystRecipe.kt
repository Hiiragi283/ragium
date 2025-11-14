package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import java.util.Optional

/**
 * [HTComplexRecipe]の抽象クラス
 */
abstract class HTItemWithCatalystRecipe(
    val required: HTItemIngredient,
    val optional: Optional<HTItemIngredient>,
    val itemResult: Optional<HTItemResult>,
    val fluidResult: Optional<HTFluidResult>,
) : HTComplexRecipe {
    final override fun getRequiredAmount(index: Int, stack: ImmutableFluidStack): Int = 0

    final override fun assembleItem(input: HTMultiRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, provider, itemResult)

    final override fun isIncomplete(): Boolean {
        val bool1: Boolean = required.hasNoMatchingStacks()
        val bool2: Boolean = optional.map(HTItemIngredient::hasNoMatchingStacks).orElse(false)
        return when {
            bool1 || bool2 -> true
            itemResult.isEmpty && fluidResult.isEmpty -> true
            else -> {
                val bool3: Boolean = itemResult.map(HTItemResult::hasNoMatchingStack).orElse(true)
                val bool4: Boolean = fluidResult.map(HTFluidResult::hasNoMatchingStack).orElse(true)
                bool3 && bool4
            }
        }
    }

    final override fun assembleFluid(input: HTMultiRecipeInput, provider: HolderLookup.Provider): ImmutableFluidStack? =
        getFluidResult(input, provider, fluidResult)
}
