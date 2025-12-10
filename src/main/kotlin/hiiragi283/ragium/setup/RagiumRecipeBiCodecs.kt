package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.extra.HTPlantingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.multi.HTRockGeneratingRecipe
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.serialization.codec.MapBiCodecs
import hiiragi283.ragium.api.serialization.codec.ParameterCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.common.data.recipe.HTCombineRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemWithCatalystRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSimulatingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleExtraItemRecipeBuilder
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTSimpleMixingRecipe
import hiiragi283.ragium.common.recipe.base.HTBasicCombineRecipe
import hiiragi283.ragium.common.recipe.base.HTBasicItemWithCatalystRecipe
import hiiragi283.ragium.common.recipe.base.HTBasicSimulatingRecipe
import hiiragi283.ragium.common.recipe.base.HTBasicSingleExtraItemRecipe
import hiiragi283.ragium.common.recipe.base.HTBasicSingleOutputRecipe
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
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
    val PLANTING: MapBiCodec<RegistryFriendlyByteBuf, HTPlantingRecipe> = MapBiCodec.composite(
        HTKeyOrTagHelper.INSTANCE
            .codec(Registries.ITEM)
            .fieldOf("seed")
            .forGetter(HTPlantingRecipe::seed),
        HTItemIngredient.UNSIZED_CODEC.fieldOf("soil").forGetter(HTPlantingRecipe::soil),
        HTFluidIngredient.CODEC.fieldOf("fluid").forGetter(HTPlantingRecipe::fluid),
        HTItemResult.CODEC.fieldOf("crop").forGetter(HTPlantingRecipe::crop),
        ::HTPlantingRecipe,
    )

    @JvmField
    val REFINING: MapBiCodec<RegistryFriendlyByteBuf, HTRefiningRecipe> = MapBiCodec.composite(
        HTItemIngredient.UNSIZED_CODEC.optionalFieldOf("item_ingredient").forGetter(HTRefiningRecipe::itemIngredient),
        HTFluidIngredient.CODEC.fieldOf("fluid_ingredient").forGetter(HTRefiningRecipe::fluidIngredient),
        RESULTS.forGetter(HTRefiningRecipe::results),
        ::HTRefiningRecipe,
    )

    @JvmField
    val ROCK_GENERATING: MapBiCodec<RegistryFriendlyByteBuf, HTRockGeneratingRecipe> = MapBiCodec.composite(
        HTFluidIngredient.CODEC.fieldOf("left").forGetter(HTRockGeneratingRecipe::left),
        BiCodecs.either(HTItemIngredient.UNSIZED_CODEC, HTFluidIngredient.CODEC).fieldOf("right").forGetter(HTRockGeneratingRecipe::right),
        HTItemIngredient.UNSIZED_CODEC.optionalFieldOf("bottom").forGetter(HTRockGeneratingRecipe::bottom),
        HTItemResult.CODEC.fieldOf(RagiumConst.RESULT).forGetter(HTRockGeneratingRecipe::result),
        ::HTRockGeneratingRecipe,
    )

    @JvmStatic
    fun <I : Any, R : HTBasicSingleOutputRecipe> singleOutput(
        factory: (I, HTItemResult) -> R,
        ingredient: ParameterCodec<in RegistryFriendlyByteBuf, R, I>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        ingredient,
        HTItemResult.CODEC.fieldOf(RagiumConst.RESULT).forGetter(HTBasicSingleOutputRecipe::result),
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
    fun <R : HTBasicSingleExtraItemRecipe> itemToExtra(
        factory: HTSingleExtraItemRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf(RagiumConst.INGREDIENT).forGetter(HTBasicSingleExtraItemRecipe::ingredient),
        HTItemResult.CODEC.fieldOf(RagiumConst.RESULT).forGetter(HTBasicSingleExtraItemRecipe::result),
        HTItemResult.CODEC.optionalFieldOf("extra").forGetter(HTBasicSingleExtraItemRecipe::extra),
        factory::create,
    )

    @JvmStatic
    fun <T : Any, R : HTBasicSimulatingRecipe<HolderSet<T>>> simulating(
        registryKey: RegistryKey<T>,
        factory: HTSimulatingRecipeBuilder.Factory<HolderSet<T>, R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = simulating(VanillaBiCodecs.holderSet(registryKey), factory)

    @JvmStatic
    fun <T : Any, R : HTBasicSimulatingRecipe<T>> simulating(
        catalyst: BiCodec<in RegistryFriendlyByteBuf, T>,
        factory: HTSimulatingRecipeBuilder.Factory<T, R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.optionalFieldOf(RagiumConst.INGREDIENT).forGetter(HTBasicSimulatingRecipe<T>::ingredient),
        catalyst.fieldOf(RagiumConst.CATALYST).forGetter(HTBasicSimulatingRecipe<T>::catalyst),
        RESULTS.forGetter(HTBasicSimulatingRecipe<T>::results),
        factory::create,
    )
}
