package hiiragi283.ragium.setup

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.MapBiCodec
import hiiragi283.ragium.api.data.recipe.impl.HTCombineItemToItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTFluidToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTFluidWithCatalystToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemWithFluidToObjRecipeBuilder
import hiiragi283.ragium.api.recipe.HTFluidToObjRecipe
import hiiragi283.ragium.api.recipe.HTFluidWithCatalystToObjRecipe
import hiiragi283.ragium.api.recipe.HTItemToObjRecipe
import hiiragi283.ragium.api.recipe.HTItemWithFluidToObjRecipe
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipeBase
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import net.minecraft.network.RegistryFriendlyByteBuf

object RagiumRecipeBiCodecs {
    @JvmStatic
    fun <R1 : HTRecipeResult<*, *>, R2 : HTItemToObjRecipe<R1>> itemToObj(
        codec: BiCodec<RegistryFriendlyByteBuf, R1>,
        factory: HTItemToObjRecipeBuilder.Factory<R1, R2>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R2> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("ingredient"),
        HTItemToObjRecipe<R1>::ingredient,
        codec.fieldOf("result"),
        HTItemToObjRecipe<R1>::result,
        factory::create,
    )

    @JvmStatic
    fun <R : HTItemToChancedItemRecipeBase> itemToChancedItem(
        factory: HTItemToChancedItemRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec
        .composite(
            HTItemIngredient.CODEC.fieldOf("ingredient"),
            HTItemToChancedItemRecipeBase::ingredient,
            HTItemResult.CODEC.listOrElement(1, 4).fieldOf("results"),
            HTItemToChancedItemRecipeBase::results,
            BiCodec.floatRange(0f, 1f).listOrElement(0, 4).optionalFieldOf("chances", listOf(1f)),
            HTItemToChancedItemRecipeBase::chances,
            factory::create,
        ).validate { recipe: R ->
            if (recipe.chances.isNotEmpty()) {
                if (recipe.chances.size != recipe.results.size) {
                    return@validate DataResult.error { "Requires the same count of results and its chances!" }
                }
            }
            DataResult.success(recipe)
        }

    @JvmStatic
    fun <R : HTCombineItemToItemRecipe> combineItemToItem(
        factory: HTCombineItemToItemRecipeBuilder.Factory<R>,
        size: IntRange,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.listOf(size).fieldOf("ingredients"),
        HTCombineItemToItemRecipe::ingredients,
        HTItemResult.CODEC.fieldOf("result"),
        HTCombineItemToItemRecipe::result,
        factory::create,
    )

    @JvmStatic
    fun <R1 : HTRecipeResult<*, *>, R2 : HTFluidWithCatalystToObjRecipe<R1>> fluidWithCatalystToObj(
        codec: BiCodec<RegistryFriendlyByteBuf, R1>,
        factory: HTFluidWithCatalystToObjRecipeBuilder.Factory<R1, R2>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R2> = MapBiCodec.composite(
        HTFluidIngredient.CODEC.fieldOf("ingredient"),
        HTFluidWithCatalystToObjRecipe<R1>::ingredient,
        HTItemIngredient.CODEC.optionalFieldOf("catalyst"),
        HTFluidWithCatalystToObjRecipe<R1>::catalyst,
        codec.fieldOf("result"),
        HTFluidWithCatalystToObjRecipe<R1>::result,
        factory::create,
    )

    @JvmStatic
    fun <R1 : HTRecipeResult<*, *>, R2 : HTItemWithFluidToObjRecipe<R1>> itemWithFluidToObj(
        codec: BiCodec<RegistryFriendlyByteBuf, R1>,
        factory: HTItemWithFluidToObjRecipeBuilder.Factory<R1, R2>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R2> = MapBiCodec.composite(
        HTItemIngredient.CODEC.optionalFieldOf("item_ingredient"),
        HTItemWithFluidToObjRecipe<R1>::itemIngredient,
        HTFluidIngredient.CODEC.optionalFieldOf("fluid_ingredient"),
        HTItemWithFluidToObjRecipe<R1>::fluidIngredient,
        codec.fieldOf("result"),
        HTItemWithFluidToObjRecipe<R1>::result,
        factory::create,
    )

    @JvmStatic
    fun <R : HTFluidToObjRecipe> fluidToObj(factory: HTFluidToObjRecipeBuilder.Factory<R>): MapBiCodec<RegistryFriendlyByteBuf, R> =
        MapBiCodec.composite(
            HTFluidIngredient.CODEC.fieldOf("ingredient"),
            HTFluidToObjRecipe::ingredient,
            HTItemResult.CODEC.optionalFieldOf("item_result"),
            HTFluidToObjRecipe::itemResult,
            HTFluidResult.CODEC.listOrElement(1, 4).fieldOf("fluid_results"),
            HTFluidToObjRecipe::fluidResults,
            factory::create,
        )
}
