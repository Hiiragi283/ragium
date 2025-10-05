package hiiragi283.ragium.setup

import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.MapBiCodec
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.HTFluidTransformRecipe
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.impl.data.recipe.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystToItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.recipe.HTAlloyingRecipe
import hiiragi283.ragium.impl.recipe.HTCrushingRecipe
import hiiragi283.ragium.impl.recipe.HTEnchantingRecipe
import hiiragi283.ragium.impl.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.impl.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.impl.recipe.base.HTItemWithFluidToChancedItemRecipeBase
import hiiragi283.ragium.impl.recipe.ingredient.HTFluidIngredientImpl
import hiiragi283.ragium.impl.recipe.ingredient.HTItemIngredientImpl
import net.minecraft.network.RegistryFriendlyByteBuf

object RagiumRecipeBiCodecs {
    @JvmField
    val ITEM_CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemIngredient> = HTItemIngredientImpl.CODEC

    @JvmField
    val FLUID_CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidIngredient> = HTFluidIngredientImpl.CODEC

    @JvmField
    val ALLOYING: MapBiCodec<RegistryFriendlyByteBuf, HTAlloyingRecipe> = MapBiCodec
        .composite(
            ITEM_CODEC.listOf(2, 3).fieldOf("ingredients"),
            HTAlloyingRecipe::ingredients,
            HTResultHelper.INSTANCE.itemCodec().fieldOf("result"),
            HTAlloyingRecipe::result,
            ::HTAlloyingRecipe,
        )

    @JvmField
    val CRUSHING: MapBiCodec<RegistryFriendlyByteBuf, HTCrushingRecipe> = MapBiCodec
        .composite(
            ITEM_CODEC.fieldOf("ingredient"),
            HTCrushingRecipe::ingredient,
            HTChancedItemRecipe.ChancedResult.CODEC
                .listOrElement(1, 4)
                .fieldOf("results"),
            HTCrushingRecipe::results,
            ::HTCrushingRecipe,
        )

    @JvmField
    val ENCHANTING: MapBiCodec<RegistryFriendlyByteBuf, HTEnchantingRecipe> = MapBiCodec
        .composite(
            ITEM_CODEC.listOf(1, 3).fieldOf("ingredient"),
            HTEnchantingRecipe::ingredients,
            HTResultHelper.INSTANCE.itemCodec().fieldOf("result"),
            HTEnchantingRecipe::result,
            ::HTEnchantingRecipe,
        )

    @JvmStatic
    fun <R : HTItemToItemRecipe> itemToItem(
        factory: HTItemToObjRecipeBuilder.Factory<HTItemResult, R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        ITEM_CODEC.fieldOf("ingredient"),
        HTItemToItemRecipe::ingredient,
        HTResultHelper.INSTANCE.itemCodec().fieldOf("result"),
        HTItemToItemRecipe::result,
        factory::create,
    )

    @JvmStatic
    fun <R : HTItemToFluidRecipe> itemToFluid(
        factory: HTItemToObjRecipeBuilder.Factory<HTFluidResult, R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        ITEM_CODEC.fieldOf("ingredient"),
        HTItemToFluidRecipe::ingredient,
        HTResultHelper.INSTANCE.fluidCodec().fieldOf("result"),
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
        HTResultHelper.INSTANCE.itemCodec().fieldOf("result"),
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
        HTChancedItemRecipe.ChancedResult.CODEC
            .listOrElement(1, 4)
            .fieldOf("results"),
        HTItemWithFluidToChancedItemRecipeBase::results,
        factory::create,
    )

    @JvmStatic
    fun <R : HTFluidTransformRecipe> fluidTransform(
        factory: HTFluidTransformRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        FLUID_CODEC.fieldOf("fluid_ingredient"),
        HTFluidTransformRecipe::fluidIngredient,
        ITEM_CODEC.optionalFieldOf("item_ingredient"),
        HTFluidTransformRecipe::itemIngredient,
        HTResultHelper.INSTANCE.itemCodec().optionalFieldOf("item_result"),
        HTFluidTransformRecipe::itemResult,
        HTResultHelper.INSTANCE.fluidCodec().optionalFieldOf("fluid_result"),
        HTFluidTransformRecipe::fluidResult,
        factory::create,
    )
}
