package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeInput

abstract class HTBasicSingleOutputRecipe<INPUT : RecipeInput>(val result: HTItemResult) : HTRecipe<INPUT> {
    final override fun assembleItem(input: INPUT, provider: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, provider, result)
}
