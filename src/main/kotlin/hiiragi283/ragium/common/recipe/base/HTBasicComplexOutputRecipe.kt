package hiiragi283.ragium.common.recipe.base

import hiiragi283.ragium.api.recipe.HTFluidRecipe
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup

abstract class HTBasicComplexOutputRecipe(val results: HTComplexResult) : HTFluidRecipe {
    final override fun assembleFluid(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableFluidStack? =
        results.getRight()?.getStackOrNull(provider)

    final override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        results.getLeft()?.getStackOrNull(provider)
}
