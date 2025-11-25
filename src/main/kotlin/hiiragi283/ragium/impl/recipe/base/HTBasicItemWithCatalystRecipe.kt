package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTItemWithCatalystRecipe
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import java.util.Optional

/**
 * [HTItemWithCatalystRecipe]の抽象クラス
 */
abstract class HTBasicItemWithCatalystRecipe(
    val required: HTItemIngredient,
    val optional: Optional<HTItemIngredient>,
    val results: HTComplexResult,
) : HTItemWithCatalystRecipe {
    final override fun assembleFluid(input: HTMultiRecipeInput, provider: HolderLookup.Provider): ImmutableFluidStack? =
        getFluidResult(input, provider, results.getRight())

    final override fun assembleItem(input: HTMultiRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, provider, results.getLeft())

    final override fun isIncomplete(): Boolean {
        val bool1: Boolean = required.hasNoMatchingStacks()
        val bool2: Boolean = optional.map(HTItemIngredient::hasNoMatchingStacks).orElse(false)
        val bool3: Boolean = results.map(HTItemResult::hasNoMatchingStack, HTFluidResult::hasNoMatchingStack)
        return bool1 || bool2 || bool3
    }
}
