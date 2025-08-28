package hiiragi283.ragium.setup

import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.MapBiCodec
import hiiragi283.ragium.api.data.recipe.impl.HTCombineItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTFluidTransformRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemToObjRecipeBuilder
import hiiragi283.ragium.api.data.recipe.impl.HTItemWithCatalystToItemRecipeBuilder
import hiiragi283.ragium.api.recipe.HTFluidTransformRecipe
import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.HTItemToObjRecipe
import hiiragi283.ragium.api.recipe.base.HTCombineItemToItemRecipe
import hiiragi283.ragium.api.recipe.base.HTItemToChancedItemRecipeBase
import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import hiiragi283.ragium.common.recipe.result.HTFluidResultImpl
import hiiragi283.ragium.common.recipe.result.HTItemResultImpl
import net.minecraft.network.RegistryFriendlyByteBuf

object RagiumRecipeBiCodecs {
    @JvmField
    val CHANCED_ITEM_RESULT: BiCodec<RegistryFriendlyByteBuf, HTItemToChancedItemRecipe.ChancedResult> = BiCodec.composite(
        HTItemResultImpl.CODEC.toMap(),
        HTItemToChancedItemRecipe.ChancedResult::base,
        BiCodec.floatRange(0f, 1f).optionalFieldOf("chance", 1f),
        HTItemToChancedItemRecipe.ChancedResult::chance,
        HTItemToChancedItemRecipe::ChancedResult,
    )

    @JvmStatic
    fun <R1 : HTRecipeResult<*>, R2 : HTItemToObjRecipe<R1>> itemToObj(
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
            CHANCED_ITEM_RESULT
                .listOrElement(1, 4)
                .fieldOf("results"),
            HTItemToChancedItemRecipeBase::results,
            factory::create,
        )

    @JvmStatic
    fun <R : HTCombineItemToItemRecipe> combineItemToObj(
        factory: HTCombineItemToObjRecipeBuilder.Factory<R>,
        size: IntRange,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.listOf(size).fieldOf("ingredients"),
        HTCombineItemToItemRecipe::ingredients,
        HTItemResultImpl.CODEC.fieldOf("result"),
        HTCombineItemToItemRecipe::result,
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
        HTItemResultImpl.CODEC.fieldOf("result"),
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
        HTItemResultImpl.CODEC.optionalFieldOf("item_result"),
        HTFluidTransformRecipe::itemResult,
        HTFluidResultImpl.CODEC.optionalFieldOf("fluid_result"),
        HTFluidTransformRecipe::fluidResult,
        factory::create,
    )
}
