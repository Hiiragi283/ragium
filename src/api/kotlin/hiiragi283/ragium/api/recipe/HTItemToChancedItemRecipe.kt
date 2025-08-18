package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

interface HTItemToChancedItemRecipe : HTRecipe<SingleRecipeInput> {
    fun getIngredientCount(input: SingleRecipeInput): Int

    fun getResultItems(input: SingleRecipeInput): List<ChancedResult>

    fun getPreviewItems(input: SingleRecipeInput, provider: HolderLookup.Provider): List<ItemStack> =
        getResultItems(input).map { it.getOrEmpty(provider) }.filterNot(ItemStack::isEmpty)

    data class ChancedResult(val base: HTItemResult, val chance: Float) : HTRecipeResult<ItemStack> by base {
        companion object {
            @JvmField
            val CODEC: BiCodec<RegistryFriendlyByteBuf, ChancedResult> = BiCodec.composite(
                HTItemResult.CODEC.toMap(),
                ChancedResult::base,
                BiCodec.floatRange(0f, 1f).optionalFieldOf("chance", 1f),
                ChancedResult::chance,
                ::ChancedResult,
            )
        }
    }
}
