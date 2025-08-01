package hiiragi283.ragium.setup

import hiiragi283.ragium.api.data.recipe.HTCombineItemToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemWithCatalystToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemWithFluidToItemRecipeBuilder
import hiiragi283.ragium.api.extension.listOf
import hiiragi283.ragium.api.extension.toOptional
import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.HTItemToItemRecipe
import hiiragi283.ragium.api.recipe.HTItemWithFluidToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

object RagiumRecipeStreamCodecs {
    @JvmStatic
    fun <R : HTItemToChancedItemRecipe> itemToChancedItem(
        factory: HTItemToChancedItemRecipeBuilder.Factory<R>,
    ): StreamCodec<RegistryFriendlyByteBuf, R> = StreamCodec.composite(
        HTItemIngredient.STREAM_CODEC,
        HTItemToChancedItemRecipe::ingredient,
        HTItemResult.STREAM_CODEC.listOf(),
        HTItemToChancedItemRecipe::results,
        ByteBufCodecs.FLOAT.listOf(),
        HTItemToChancedItemRecipe::chances,
        factory::create,
    )

    @JvmStatic
    fun <R : HTItemToItemRecipe> itemToItem(factory: HTItemToItemRecipeBuilder.Factory<R>): StreamCodec<RegistryFriendlyByteBuf, R> =
        StreamCodec.composite(
            HTItemIngredient.STREAM_CODEC,
            HTItemToItemRecipe::ingredient,
            HTItemResult.STREAM_CODEC,
            HTItemToItemRecipe::result,
            factory::create,
        )

    @JvmStatic
    fun <R : HTCombineItemToItemRecipe> combineItemToItem(
        factory: HTCombineItemToItemRecipeBuilder.Factory<R>,
    ): StreamCodec<RegistryFriendlyByteBuf, R> = StreamCodec.composite(
        HTItemIngredient.STREAM_CODEC.listOf(),
        HTCombineItemToItemRecipe::ingredients,
        HTItemResult.STREAM_CODEC,
        HTCombineItemToItemRecipe::result,
        factory::create,
    )

    @JvmStatic
    fun <R : HTItemWithCatalystToItemRecipe> itemWithCatalystToItem(
        factory: HTItemWithCatalystToItemRecipeBuilder.Factory<R>,
    ): StreamCodec<RegistryFriendlyByteBuf, R> = StreamCodec.composite(
        HTItemIngredient.STREAM_CODEC,
        HTItemWithCatalystToItemRecipe::ingredient,
        HTItemIngredient.STREAM_CODEC.toOptional(),
        HTItemWithCatalystToItemRecipe::catalyst,
        HTItemResult.STREAM_CODEC,
        HTItemWithCatalystToItemRecipe::result,
        factory::create,
    )

    @JvmStatic
    fun <R : HTItemWithFluidToItemRecipe> itemWithFluidToItem(
        factory: HTItemWithFluidToItemRecipeBuilder.Factory<R>,
    ): StreamCodec<RegistryFriendlyByteBuf, R> = StreamCodec.composite(
        HTItemIngredient.STREAM_CODEC.toOptional(),
        HTItemWithFluidToItemRecipe::itemIngredient,
        HTFluidIngredient.STREAM_CODEC.toOptional(),
        HTItemWithFluidToItemRecipe::fluidIngredient,
        HTItemResult.STREAM_CODEC,
        HTItemWithFluidToItemRecipe::result,
        factory::create,
    )
}
