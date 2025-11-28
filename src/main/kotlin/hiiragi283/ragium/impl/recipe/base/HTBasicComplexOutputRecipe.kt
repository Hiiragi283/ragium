package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.HTFluidRecipe
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeInput

abstract class HTBasicComplexOutputRecipe<INPUT : RecipeInput>(val results: HTComplexResult) : HTFluidRecipe<INPUT> {
    final override fun assembleFluid(input: INPUT, provider: HolderLookup.Provider): ImmutableFluidStack? =
        getFluidResult(input, provider, results.getRight())

    final override fun assembleItem(input: INPUT, provider: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, provider, results.getLeft())

    final override fun isIncomplete(): Boolean =
        isIncompleteIngredient() || results.map(HTItemResult::hasNoMatchingStack, HTFluidResult::hasNoMatchingStack)

    /**
     * 材料が有効かどうか判定します
     */
    protected abstract fun isIncompleteIngredient(): Boolean
}
