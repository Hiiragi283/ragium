package hiiragi283.ragium.common.recipe.base

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup

abstract class HTBasicSingleOutputRecipe(val result: HTItemResult) : HTRecipe {
    final override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        result.getStackOrNull(provider)
}
