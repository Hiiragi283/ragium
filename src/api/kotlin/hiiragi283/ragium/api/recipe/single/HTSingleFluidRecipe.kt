package hiiragi283.ragium.api.recipe.single

import hiiragi283.ragium.api.recipe.HTFluidRecipe
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * 単一のアイテムから単一の液体を生産するレシピ
 */
interface HTSingleFluidRecipe :
    HTSingleItemRecipe,
    HTFluidRecipe<SingleRecipeInput> {
    override fun assembleItem(input: SingleRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? = null
}
