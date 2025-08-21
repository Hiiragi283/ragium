package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

interface HTItemToChancedItemRecipe : HTRecipe<SingleRecipeInput> {
    fun getIngredientCount(input: SingleRecipeInput): Int

    fun getResultItems(input: SingleRecipeInput): List<ChancedResult>

    fun getPreviewItems(input: SingleRecipeInput, provider: HolderLookup.Provider): List<ItemStack> =
        getResultItems(input).map { it.getOrEmpty(provider) }.filterNot(ItemStack::isEmpty)

    data class ChancedResult(val base: HTItemResult, val chance: Float) : HTItemResult by base
}
