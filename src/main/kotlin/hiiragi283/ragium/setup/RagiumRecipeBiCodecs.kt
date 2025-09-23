package hiiragi283.ragium.setup

import hiiragi283.ragium.api.codec.BiCodecs
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
import hiiragi283.ragium.impl.recipe.HTAlloyingRecipe
import hiiragi283.ragium.impl.recipe.HTBrewingRecipe
import hiiragi283.ragium.impl.recipe.HTCrushingRecipe
import hiiragi283.ragium.impl.recipe.HTEnchantingRecipe
import hiiragi283.ragium.impl.recipe.HTWashingRecipe
import hiiragi283.ragium.impl.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.impl.recipe.base.HTItemToItemRecipe
import net.minecraft.network.RegistryFriendlyByteBuf

object RagiumRecipeBiCodecs {
    @JvmField
    val ALLOYING: MapBiCodec<RegistryFriendlyByteBuf, HTAlloyingRecipe> = MapBiCodec
        .composite(
            HTItemIngredient.CODEC.listOf(2, 3).fieldOf("ingredient"),
            HTAlloyingRecipe::ingredients,
            HTResultHelper.INSTANCE.itemCodec().fieldOf("result"),
            HTAlloyingRecipe::result,
            ::HTAlloyingRecipe,
        )

    @JvmField
    val BREWING: MapBiCodec<RegistryFriendlyByteBuf, HTBrewingRecipe> = MapBiCodec
        .composite(
            HTItemIngredient.CODEC.listOf(1, 3).fieldOf("ingredient"),
            HTBrewingRecipe::ingredients,
            BiCodecs.POTION.fieldOf("potion"),
            HTBrewingRecipe::potion,
            ::HTBrewingRecipe,
        )

    @JvmField
    val CRUSHING: MapBiCodec<RegistryFriendlyByteBuf, HTCrushingRecipe> = MapBiCodec
        .composite(
            HTItemIngredient.CODEC.fieldOf("ingredient"),
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
            HTItemIngredient.CODEC.listOf(1, 3).fieldOf("ingredient"),
            HTEnchantingRecipe::ingredients,
            HTResultHelper.INSTANCE.itemCodec().fieldOf("result"),
            HTEnchantingRecipe::result,
            ::HTEnchantingRecipe,
        )

    @JvmField
    val WASHING: MapBiCodec<RegistryFriendlyByteBuf, HTWashingRecipe> = MapBiCodec
        .composite(
            HTItemIngredient.CODEC.fieldOf("item_ingredient"),
            HTWashingRecipe::ingredient,
            HTFluidIngredient.CODEC.fieldOf("fluid_ingredient"),
            HTWashingRecipe::fluidIngredient,
            HTChancedItemRecipe.ChancedResult.CODEC
                .listOrElement(1, 4)
                .fieldOf("results"),
            HTWashingRecipe::results,
            ::HTWashingRecipe,
        )

    @JvmStatic
    fun <R : HTItemToItemRecipe> itemToItem(
        factory: HTItemToObjRecipeBuilder.Factory<HTItemResult, R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("ingredient"),
        HTItemToItemRecipe::ingredient,
        HTResultHelper.INSTANCE.itemCodec().fieldOf("result"),
        HTItemToItemRecipe::result,
        factory::create,
    )

    @JvmStatic
    fun <R : HTItemToFluidRecipe> itemToFluid(
        factory: HTItemToObjRecipeBuilder.Factory<HTFluidResult, R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("ingredient"),
        HTItemToFluidRecipe::ingredient,
        HTResultHelper.INSTANCE.fluidCodec().fieldOf("result"),
        HTItemToFluidRecipe::result,
        factory::create,
    )

    @JvmStatic
    fun <R : HTItemWithCatalystToItemRecipe> itemWithCatalystToItem(
        factory: HTItemWithCatalystToItemRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.optionalFieldOf("ingredient"),
        HTItemWithCatalystToItemRecipe::ingredient,
        HTItemIngredient.CODEC.fieldOf("catalyst"),
        HTItemWithCatalystToItemRecipe::catalyst,
        HTResultHelper.INSTANCE.itemCodec().fieldOf("result"),
        HTItemWithCatalystToItemRecipe::result,
        factory::create,
    )

    @JvmStatic
    fun <R : HTFluidTransformRecipe> fluidTransform(
        factory: HTFluidTransformRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTFluidIngredient.CODEC.fieldOf("fluid_ingredient"),
        HTFluidTransformRecipe::fluidIngredient,
        HTItemIngredient.CODEC.optionalFieldOf("item_ingredient"),
        HTFluidTransformRecipe::itemIngredient,
        HTResultHelper.INSTANCE.itemCodec().optionalFieldOf("item_result"),
        HTFluidTransformRecipe::itemResult,
        HTResultHelper.INSTANCE.fluidCodec().optionalFieldOf("fluid_result"),
        HTFluidTransformRecipe::fluidResult,
        factory::create,
    )
}
