package hiiragi283.ragium.setup

import hiiragi283.ragium.api.data.recipe.HTCombineItemToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTFluidToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemWithCatalystToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.HTItemWithFluidToObjRecipeBuilder
import hiiragi283.ragium.api.extension.listOf
import hiiragi283.ragium.api.extension.toOptional
import hiiragi283.ragium.api.recipe.HTFluidToObjRecipe
import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.HTItemToObjRecipe
import hiiragi283.ragium.api.recipe.HTItemWithFluidToObjRecipe
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
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
    fun <R1 : HTRecipeResult<*, *>, R2 : HTItemToObjRecipe<R1>> itemToObj(
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, R1>,
        factory: HTItemToObjRecipeBuilder.Factory<R1, R2>,
    ): StreamCodec<RegistryFriendlyByteBuf, R2> = StreamCodec.composite(
        HTItemIngredient.STREAM_CODEC,
        HTItemToObjRecipe<R1>::ingredient,
        streamCodec,
        HTItemToObjRecipe<R1>::result,
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
    fun <R1 : HTRecipeResult<*, *>, R2 : HTItemWithFluidToObjRecipe<R1>> itemWithFluidToObj(
        streamCodec: StreamCodec<RegistryFriendlyByteBuf, R1>,
        factory: HTItemWithFluidToObjRecipeBuilder.Factory<R1, R2>,
    ): StreamCodec<RegistryFriendlyByteBuf, R2> = StreamCodec.composite(
        HTItemIngredient.STREAM_CODEC.toOptional(),
        HTItemWithFluidToObjRecipe<R1>::itemIngredient,
        HTFluidIngredient.STREAM_CODEC.toOptional(),
        HTItemWithFluidToObjRecipe<R1>::fluidIngredient,
        streamCodec,
        HTItemWithFluidToObjRecipe<R1>::result,
        factory::create,
    )

    @JvmStatic
    fun <R : HTFluidToObjRecipe> fluidToObj(factory: HTFluidToObjRecipeBuilder.Factory<R>): StreamCodec<RegistryFriendlyByteBuf, R> =
        StreamCodec.composite(
            HTFluidIngredient.STREAM_CODEC,
            HTFluidToObjRecipe::ingredient,
            HTItemResult.STREAM_CODEC.toOptional(),
            HTFluidToObjRecipe::itemResult,
            HTFluidResult.STREAM_CODEC.listOf(),
            HTFluidToObjRecipe::fluidResults,
            factory::create,
        )
}
