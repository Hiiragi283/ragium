package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.serialization.codec.MapBiCodecs
import hiiragi283.ragium.api.serialization.codec.ParameterCodec
import hiiragi283.ragium.impl.data.recipe.HTCombineRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.recipe.HTMeltingRecipe
import hiiragi283.ragium.impl.recipe.HTMixingRecipe
import hiiragi283.ragium.impl.recipe.HTRefiningRecipe
import hiiragi283.ragium.impl.recipe.HTSimpleMixingRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicCombineRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicItemToChancedItemRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicItemWithCatalystRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicSingleOutputRecipe
import net.minecraft.network.RegistryFriendlyByteBuf

object RagiumRecipeBiCodecs {
    @JvmField
    val RESULTS: MapBiCodec<RegistryFriendlyByteBuf, HTComplexResult> = MapBiCodecs.ior(
        HTItemResult.CODEC.optionalFieldOf(RagiumConst.ITEM_RESULT),
        HTFluidResult.CODEC.optionalFieldOf(RagiumConst.FLUID_RESULT),
    )

    @JvmField
    val MELTING: MapBiCodec<RegistryFriendlyByteBuf, HTMeltingRecipe> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf(RagiumConst.INGREDIENT).forGetter(HTMeltingRecipe::ingredient),
        HTFluidResult.CODEC.fieldOf(RagiumConst.RESULT).forGetter(HTMeltingRecipe::result),
        ::HTMeltingRecipe,
    )

    @JvmField
    val MIXING: MapBiCodec<RegistryFriendlyByteBuf, HTMixingRecipe> = MapBiCodec.composite(
        HTItemIngredient.CODEC
            .listOrElement(0, 4)
            .optionalFieldOf("item_ingredients", listOf())
            .forGetter(HTMixingRecipe::itemIngredients),
        HTFluidIngredient.CODEC
            .nonEmptyListOf(2)
            .fieldOf("fluid_ingredients")
            .forGetter(HTMixingRecipe::fluidIngredients),
        RESULTS.forGetter(HTMixingRecipe::results),
        ::HTMixingRecipe,
    )

    @JvmField
    val MIXING_SIMPLE: MapBiCodec<RegistryFriendlyByteBuf, HTSimpleMixingRecipe> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("item_ingredient").forGetter(HTSimpleMixingRecipe::itemIngredient),
        HTFluidIngredient.CODEC.fieldOf("fluid_ingredient").forGetter(HTSimpleMixingRecipe::fluidIngredient),
        RESULTS.forGetter(HTSimpleMixingRecipe::results),
        ::HTSimpleMixingRecipe,
    )

    @JvmField
    val REFINING: MapBiCodec<RegistryFriendlyByteBuf, HTRefiningRecipe> = MapBiCodec.composite(
        HTItemIngredient.CODEC.optionalFieldOf("item_ingredient").forGetter(HTRefiningRecipe::itemIngredient),
        HTFluidIngredient.CODEC.fieldOf("fluid_ingredient").forGetter(HTRefiningRecipe::fluidIngredient),
        RESULTS.forGetter(HTRefiningRecipe::results),
        ::HTRefiningRecipe,
    )

    @JvmStatic
    fun <I : Any, R : HTBasicSingleOutputRecipe<*>> singleOutput(
        factory: (I, HTItemResult) -> R,
        ingredient: ParameterCodec<in RegistryFriendlyByteBuf, R, I>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        ingredient,
        HTItemResult.CODEC.fieldOf(RagiumConst.RESULT).forGetter(HTBasicSingleOutputRecipe<*>::result),
        factory,
    )

    @JvmStatic
    fun <RESULT : Any, R : HTBasicCombineRecipe> combine(
        factory: HTCombineRecipeBuilder.Factory<RESULT, R>,
        resultCodec: ParameterCodec<in RegistryFriendlyByteBuf, R, RESULT>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        MapBiCodecs
            .pair(HTItemIngredient.CODEC.fieldOf("left"), HTItemIngredient.CODEC.fieldOf("right"))
            .forGetter(HTBasicCombineRecipe::itemIngredients),
        resultCodec,
        factory::create,
    )

    @JvmStatic
    fun <R : HTBasicItemWithCatalystRecipe> itemWithCatalyst(
        factory: HTItemWithCatalystRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("required").forGetter(HTBasicItemWithCatalystRecipe::required),
        HTItemIngredient.CODEC.optionalFieldOf("optional").forGetter(HTBasicItemWithCatalystRecipe::optional),
        RESULTS.forGetter(HTBasicItemWithCatalystRecipe::results),
        factory::create,
    )

    @JvmStatic
    fun <R : HTBasicItemToChancedItemRecipe> itemToChanced(
        factory: HTItemToChancedItemRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf(RagiumConst.INGREDIENT).forGetter(HTBasicItemToChancedItemRecipe::ingredient),
        HTItemResultWithChance.CODEC
            .nonEmptyListOf(4)
            .fieldOf(RagiumConst.RESULTS)
            .forGetter(HTBasicItemToChancedItemRecipe::results),
        factory::create,
    )

    @JvmStatic
    fun <R : HTBasicItemWithFluidToChancedItemRecipe> itemWithFluidToChanced(
        factory: HTItemWithFluidToChancedItemRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("item_ingredient").forGetter(HTBasicItemWithFluidToChancedItemRecipe::ingredient),
        HTFluidIngredient.CODEC.fieldOf("fluid_ingredient").forGetter(HTBasicItemWithFluidToChancedItemRecipe::fluidIngredient),
        HTItemResultWithChance.CODEC
            .nonEmptyListOf(4)
            .fieldOf("results")
            .forGetter(HTBasicItemWithFluidToChancedItemRecipe::results),
        factory::create,
    )
}
