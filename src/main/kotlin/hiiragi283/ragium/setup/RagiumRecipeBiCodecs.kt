package hiiragi283.ragium.setup

import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTChancedItemResult
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.impl.data.recipe.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystToItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.recipe.HTAlloyingRecipe
import hiiragi283.ragium.impl.recipe.HTCrushingRecipe
import hiiragi283.ragium.impl.recipe.HTEnchantingRecipe
import hiiragi283.ragium.impl.recipe.base.HTFluidTransformRecipeBase
import hiiragi283.ragium.impl.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.impl.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.impl.recipe.base.HTItemWithFluidToChancedItemRecipeBase
import net.minecraft.network.RegistryFriendlyByteBuf

object RagiumRecipeBiCodecs {
    @JvmField
    val ITEM_CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemIngredient> = HTItemIngredient.CODEC

    @JvmField
    val FLUID_CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidIngredient> = HTFluidIngredient.CODEC

    @JvmField
    val ALLOYING: MapBiCodec<RegistryFriendlyByteBuf, HTAlloyingRecipe> = MapBiCodec
        .composite(
            ITEM_CODEC.listOf(2, 3).fieldOf("ingredients"),
            HTAlloyingRecipe::ingredients,
            HTItemResult.CODEC.fieldOf("result"),
            HTAlloyingRecipe::result,
            ::HTAlloyingRecipe,
        )

    @JvmField
    val CRUSHING: MapBiCodec<RegistryFriendlyByteBuf, HTCrushingRecipe> = MapBiCodec
        .composite(
            ITEM_CODEC.fieldOf("ingredient"),
            HTCrushingRecipe::ingredient,
            HTChancedItemResult.CODEC.listOrElement(1, 4).fieldOf("results"),
            HTCrushingRecipe::results,
            ::HTCrushingRecipe,
        )

    @JvmField
    val ENCHANTING: MapBiCodec<RegistryFriendlyByteBuf, HTEnchantingRecipe> = MapBiCodec
        .composite(
            ITEM_CODEC.listOf(1, 3).fieldOf("ingredient"),
            HTEnchantingRecipe::ingredients,
            HTItemResult.CODEC.fieldOf("result"),
            HTEnchantingRecipe::result,
            ::HTEnchantingRecipe,
        )

    @JvmStatic
    fun <R : HTItemToItemRecipe> itemToItem(
        factory: HTItemToObjRecipeBuilder.Factory<HTItemResult, R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        ITEM_CODEC.fieldOf("ingredient"),
        HTItemToItemRecipe::ingredient,
        HTItemResult.CODEC.fieldOf("result"),
        HTItemToItemRecipe::result,
        factory::create,
    )

    @JvmStatic
    fun <R : HTItemToFluidRecipe> itemToFluid(
        factory: HTItemToObjRecipeBuilder.Factory<HTFluidResult, R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        ITEM_CODEC.fieldOf("ingredient"),
        HTItemToFluidRecipe::ingredient,
        HTFluidResult.CODEC.fieldOf("result"),
        HTItemToFluidRecipe::result,
        factory::create,
    )

    @JvmStatic
    fun <R : HTItemWithCatalystToItemRecipe> itemWithCatalystToItem(
        factory: HTItemWithCatalystToItemRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        ITEM_CODEC.optionalFieldOf("ingredient"),
        HTItemWithCatalystToItemRecipe::ingredient,
        ITEM_CODEC.fieldOf("catalyst"),
        HTItemWithCatalystToItemRecipe::catalyst,
        HTItemResult.CODEC.fieldOf("result"),
        HTItemWithCatalystToItemRecipe::result,
        factory::create,
    )

    @JvmStatic
    fun <R : HTItemWithFluidToChancedItemRecipeBase> itemWithFluidToChanced(
        factory: HTItemWithFluidToChancedItemRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        ITEM_CODEC.fieldOf("item_ingredient"),
        HTItemWithFluidToChancedItemRecipeBase::ingredient,
        FLUID_CODEC.fieldOf("fluid_ingredient"),
        HTItemWithFluidToChancedItemRecipeBase::fluidIngredient,
        HTChancedItemResult.CODEC.listOrElement(1, 4).fieldOf("results"),
        HTItemWithFluidToChancedItemRecipeBase::results,
        factory::create,
    )

    @JvmStatic
    fun <R : HTFluidTransformRecipeBase> fluidTransform(
        factory: HTFluidTransformRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        FLUID_CODEC.fieldOf("fluid_ingredient"),
        HTFluidTransformRecipeBase::fluidIngredient,
        ITEM_CODEC.optionalFieldOf("item_ingredient"),
        HTFluidTransformRecipeBase::itemIngredient,
        HTItemResult.CODEC.optionalFieldOf("item_result"),
        HTFluidTransformRecipeBase::itemResult,
        HTFluidResult.CODEC.optionalFieldOf("fluid_result"),
        HTFluidTransformRecipeBase::fluidResult,
        factory::create,
    )
}
