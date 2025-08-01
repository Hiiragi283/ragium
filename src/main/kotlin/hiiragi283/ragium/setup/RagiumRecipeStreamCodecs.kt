package hiiragi283.ragium.setup

import hiiragi283.ragium.api.extension.listOf
import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.HTItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

object RagiumRecipeStreamCodecs {
    @JvmStatic
    fun <R : HTItemToChancedItemRecipe> itemToChancedItem(
        factory: (HTItemIngredient, List<HTItemResult>, List<Float>) -> R,
    ): StreamCodec<RegistryFriendlyByteBuf, R> = StreamCodec.composite(
        HTItemIngredient.STREAM_CODEC,
        HTItemToChancedItemRecipe::ingredient,
        HTItemResult.STREAM_CODEC.listOf(),
        HTItemToChancedItemRecipe::results,
        ByteBufCodecs.FLOAT.listOf(),
        HTItemToChancedItemRecipe::chances,
        factory,
    )

    @JvmStatic
    fun <R : HTItemToItemRecipe> itemToItem(factory: (HTItemIngredient, HTItemResult) -> R): StreamCodec<RegistryFriendlyByteBuf, R> =
        StreamCodec.composite(
            HTItemIngredient.STREAM_CODEC,
            HTItemToItemRecipe::ingredient,
            HTItemResult.STREAM_CODEC,
            HTItemToItemRecipe::result,
            factory,
        )

    @JvmStatic
    fun <R : HTCombineItemToItemRecipe> combineItemToItem(
        factory: (List<HTItemIngredient>, HTItemResult) -> R,
    ): StreamCodec<RegistryFriendlyByteBuf, R> = StreamCodec.composite(
        HTItemIngredient.STREAM_CODEC.listOf(),
        HTCombineItemToItemRecipe::ingredients,
        HTItemResult.STREAM_CODEC,
        HTCombineItemToItemRecipe::result,
        factory,
    )
}
