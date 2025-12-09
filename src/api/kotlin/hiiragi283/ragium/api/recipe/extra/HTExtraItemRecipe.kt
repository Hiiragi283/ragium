package hiiragi283.ragium.api.recipe.extra

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup

/**
 * 主産物と副産物を生産するレシピ
 */
interface HTExtraItemRecipe : HTRecipe {
    fun assembleExtraItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack?
}
