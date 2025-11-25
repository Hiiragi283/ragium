package hiiragi283.ragium.setup

import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.serialization.codec.MapBiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.impl.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.recipe.HTBrewingRecipe
import hiiragi283.ragium.impl.recipe.HTEnchantingRecipe
import hiiragi283.ragium.impl.recipe.HTMeltingRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicComplexRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicItemToChancedItemRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicItemWithCatalystRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicSingleOutputRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf

object RagiumRecipeBiCodecs {
    @JvmField
    val RESULTS: MapBiCodec<RegistryFriendlyByteBuf, HTComplexResult> = MapBiCodecs.ior(
        HTItemResult.CODEC.optionalFieldOf("item_result"),
        HTFluidResult.CODEC.optionalFieldOf("fluid_result"),
    )

    @JvmField
    val BREWING: MapBiCodec<RegistryFriendlyByteBuf, HTBrewingRecipe> = MapBiCodec
        .composite(
            HTItemIngredient.CODEC.fieldOf("ingredient"),
            HTBrewingRecipe::ingredient,
            VanillaBiCodecs.POTION.fieldOf("potion"),
            HTBrewingRecipe::contents,
            ::HTBrewingRecipe,
        )

    @JvmField
    val ENCHANTING: MapBiCodec<RegistryFriendlyByteBuf, HTEnchantingRecipe> = MapBiCodec
        .composite(
            HTItemIngredient.CODEC.fieldOf("ingredient"),
            HTEnchantingRecipe::ingredient,
            VanillaBiCodecs.holder(Registries.ENCHANTMENT).fieldOf("enchantment"),
            HTEnchantingRecipe::holder,
            ::HTEnchantingRecipe,
        )

    @JvmField
    val MELTING: MapBiCodec<RegistryFriendlyByteBuf, HTMeltingRecipe> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("ingredient"),
        HTMeltingRecipe::ingredient,
        HTFluidResult.CODEC.fieldOf("result"),
        HTMeltingRecipe::result,
        ::HTMeltingRecipe,
    )

    @JvmStatic
    fun <I : Any, R : HTBasicSingleOutputRecipe<*>> singleOutput(
        factory: (I, HTItemResult) -> R,
        ingredient: MapBiCodec<in RegistryFriendlyByteBuf, I>,
        ingredientGetter: (R) -> I,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        ingredient,
        ingredientGetter,
        HTItemResult.CODEC.fieldOf("result"),
        HTBasicSingleOutputRecipe<*>::result,
        factory,
    )

    @JvmStatic
    fun <R : HTBasicItemWithCatalystRecipe> itemWithCatalyst(
        factory: HTItemWithCatalystRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("required"),
        HTBasicItemWithCatalystRecipe::required,
        HTItemIngredient.CODEC.optionalFieldOf("optional"),
        HTBasicItemWithCatalystRecipe::optional,
        RESULTS,
        HTBasicItemWithCatalystRecipe::results,
        factory::create,
    )

    @JvmStatic
    fun <R : HTBasicItemToChancedItemRecipe> itemToChanced(
        factory: HTItemToChancedItemRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("ingredient"),
        HTBasicItemToChancedItemRecipe::ingredient,
        HTItemResultWithChance.CODEC.listOrElement(1, 4).fieldOf("results"),
        HTBasicItemToChancedItemRecipe::results,
        factory::create,
    )

    @JvmStatic
    fun <R : HTBasicItemWithFluidToChancedItemRecipe> itemWithFluidToChanced(
        factory: HTItemWithFluidToChancedItemRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("item_ingredient"),
        HTBasicItemWithFluidToChancedItemRecipe::ingredient,
        HTFluidIngredient.CODEC.fieldOf("fluid_ingredient"),
        HTBasicItemWithFluidToChancedItemRecipe::fluidIngredient,
        HTItemResultWithChance.CODEC.listOrElement(1, 4).fieldOf("results"),
        HTBasicItemWithFluidToChancedItemRecipe::results,
        factory::create,
    )

    @JvmStatic
    fun <R : HTBasicComplexRecipe> complex(
        factory: HTComplexRecipeBuilder.Factory<R>,
        itemRange: IntRange,
        fluidRange: IntRange,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec
        .composite(
            HTItemIngredient.CODEC.listOrElement(itemRange).optionalFieldOf("item_ingredients", listOf()),
            HTBasicComplexRecipe::itemIngredients,
            HTFluidIngredient.CODEC.listOrElement(fluidRange).optionalFieldOf("fluid_ingredients", listOf()),
            HTBasicComplexRecipe::fluidIngredients,
            RESULTS,
            HTBasicComplexRecipe::results,
            factory::create,
        )
}
