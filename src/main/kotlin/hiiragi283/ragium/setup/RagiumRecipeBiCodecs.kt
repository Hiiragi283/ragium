package hiiragi283.ragium.setup

import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.impl.data.recipe.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.recipe.HTAlloyingRecipe
import hiiragi283.ragium.impl.recipe.HTEnchantingRecipe
import hiiragi283.ragium.impl.recipe.HTMixingRecipe
import hiiragi283.ragium.impl.recipe.base.HTFluidTransformRecipeBase
import hiiragi283.ragium.impl.recipe.base.HTItemToChancedItemRecipeBase
import hiiragi283.ragium.impl.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.impl.recipe.base.HTItemToItemRecipe
import hiiragi283.ragium.impl.recipe.base.HTItemWithCatalystRecipe
import hiiragi283.ragium.impl.recipe.base.HTItemWithFluidToChancedItemRecipeBase
import net.minecraft.network.RegistryFriendlyByteBuf

object RagiumRecipeBiCodecs {
    @JvmField
    val ALLOYING: MapBiCodec<RegistryFriendlyByteBuf, HTAlloyingRecipe> = MapBiCodec
        .composite(
            HTItemIngredient.CODEC.listOf(2, 3).fieldOf("ingredients"),
            HTAlloyingRecipe::ingredients,
            HTItemResult.CODEC.fieldOf("result"),
            HTAlloyingRecipe::result,
            ::HTAlloyingRecipe,
        )

    @JvmField
    val ENCHANTING: MapBiCodec<RegistryFriendlyByteBuf, HTEnchantingRecipe> = MapBiCodec
        .composite(
            HTItemIngredient.CODEC.listOf(1, 3).fieldOf("ingredient"),
            HTEnchantingRecipe::ingredients,
            HTItemResult.CODEC.fieldOf("result"),
            HTEnchantingRecipe::result,
            ::HTEnchantingRecipe,
        )

    @JvmField
    val MIXING: MapBiCodec<RegistryFriendlyByteBuf, HTMixingRecipe> = MapBiCodec
        .composite(
            HTItemIngredient.CODEC.listOf(0, 2).optionalFieldOf("item_ingredients", listOf()),
            HTMixingRecipe::itemIngredients,
            HTFluidIngredient.CODEC.listOf(1, 2).optionalFieldOf("fluid_ingredients", listOf()),
            HTMixingRecipe::fluidIngredients,
            HTItemResult.CODEC.optionalFieldOf("item_result"),
            HTMixingRecipe::itemResult,
            HTFluidResult.CODEC.optionalFieldOf("fluid_result"),
            HTMixingRecipe::fluidResult,
            ::HTMixingRecipe,
        )

    @JvmStatic
    fun <R : HTItemToItemRecipe> itemToItem(
        factory: HTItemToObjRecipeBuilder.Factory<HTItemResult, R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("ingredient"),
        HTItemToItemRecipe::ingredient,
        HTItemResult.CODEC.fieldOf("result"),
        HTItemToItemRecipe::result,
        factory::create,
    )

    @JvmStatic
    fun <R : HTItemToFluidRecipe> itemToFluid(
        factory: HTItemToObjRecipeBuilder.Factory<HTFluidResult, R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("ingredient"),
        HTItemToFluidRecipe::ingredient,
        HTFluidResult.CODEC.fieldOf("result"),
        HTItemToFluidRecipe::result,
        factory::create,
    )

    @JvmStatic
    fun <R : HTItemWithCatalystRecipe> itemWithCatalystToMulti(
        factory: HTItemWithCatalystRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("required"),
        HTItemWithCatalystRecipe::required,
        HTItemIngredient.CODEC.optionalFieldOf("optional"),
        HTItemWithCatalystRecipe::optional,
        HTItemResult.CODEC.optionalFieldOf("item_result"),
        HTItemWithCatalystRecipe::itemResult,
        HTFluidResult.CODEC.optionalFieldOf("fluid_result"),
        HTItemWithCatalystRecipe::fluidResult,
        factory::create,
    )

    @JvmStatic
    fun <R : HTItemToChancedItemRecipeBase> itemToChanced(
        factory: HTItemToChancedItemRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("ingredient"),
        HTItemToChancedItemRecipeBase::ingredient,
        HTItemResultWithChance.CODEC.listOrElement(1, 4).fieldOf("results"),
        HTItemToChancedItemRecipeBase::results,
        factory::create,
    )

    @JvmStatic
    fun <R : HTItemWithFluidToChancedItemRecipeBase> itemWithFluidToChanced(
        factory: HTItemWithFluidToChancedItemRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("item_ingredient"),
        HTItemWithFluidToChancedItemRecipeBase::ingredient,
        HTFluidIngredient.CODEC.fieldOf("fluid_ingredient"),
        HTItemWithFluidToChancedItemRecipeBase::fluidIngredient,
        HTItemResultWithChance.CODEC.listOrElement(1, 4).fieldOf("results"),
        HTItemWithFluidToChancedItemRecipeBase::results,
        factory::create,
    )

    @JvmStatic
    fun <R : HTFluidTransformRecipeBase> fluidTransform(
        factory: HTFluidTransformRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTFluidIngredient.CODEC.fieldOf("fluid_ingredient"),
        HTFluidTransformRecipeBase::fluidIngredient,
        HTItemIngredient.CODEC.optionalFieldOf("item_ingredient"),
        HTFluidTransformRecipeBase::itemIngredient,
        HTItemResult.CODEC.optionalFieldOf("item_result"),
        HTFluidTransformRecipeBase::itemResult,
        HTFluidResult.CODEC.optionalFieldOf("fluid_result"),
        HTFluidTransformRecipeBase::fluidResult,
        factory::create,
    )
}
