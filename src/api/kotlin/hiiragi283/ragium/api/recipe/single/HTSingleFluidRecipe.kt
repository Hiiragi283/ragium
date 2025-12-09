package hiiragi283.ragium.api.recipe.single

import hiiragi283.ragium.api.recipe.HTFluidRecipe
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup

/**
 * 単一のアイテムから単一の液体を生産するレシピ
 */
interface HTSingleFluidRecipe :
    HTSingleItemRecipe,
    HTFluidRecipe {
    override fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? = null
}
