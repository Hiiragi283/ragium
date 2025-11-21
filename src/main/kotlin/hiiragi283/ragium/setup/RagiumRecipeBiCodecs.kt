package hiiragi283.ragium.setup

import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.serialization.codec.MapBiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.impl.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemToObjRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithFluidToChancedItemRecipeBuilder
import hiiragi283.ragium.impl.recipe.HTAlloyingRecipe
import hiiragi283.ragium.impl.recipe.HTBrewingRecipe
import hiiragi283.ragium.impl.recipe.HTEnchantingRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicComplexRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicItemToChancedItemRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicSingleFluidRecipe
import hiiragi283.ragium.impl.recipe.base.HTBasicSingleItemRecipe
import hiiragi283.ragium.impl.recipe.base.HTItemWithCatalystRecipe
import net.minecraft.network.RegistryFriendlyByteBuf

object RagiumRecipeBiCodecs {
    @JvmField
    val RESULTS: MapBiCodec<RegistryFriendlyByteBuf, Ior<HTItemResult, HTFluidResult>> = MapBiCodecs.ior(
        HTItemResult.CODEC.optionalFieldOf("item_result"),
        HTFluidResult.CODEC.optionalFieldOf("fluid_result"),
    )

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
            HTItemIngredient.CODEC.fieldOf("item_ingredient"),
            HTEnchantingRecipe::itemIngredient,
            HTFluidIngredient.CODEC.fieldOf("fluid_ingredient"),
            HTEnchantingRecipe::fluidIngredient,
            HTItemResult.CODEC.fieldOf("result"),
            HTEnchantingRecipe::result,
            ::HTEnchantingRecipe,
        )

    @JvmStatic
    fun <R : HTBasicSingleItemRecipe> itemToItem(
        factory: HTItemToObjRecipeBuilder.Factory<HTItemResult, R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("ingredient"),
        HTBasicSingleItemRecipe::ingredient,
        HTItemResult.CODEC.fieldOf("result"),
        HTBasicSingleItemRecipe::result,
        factory::create,
    )

    @JvmStatic
    fun <R : HTBasicSingleFluidRecipe> itemToFluid(
        factory: HTItemToObjRecipeBuilder.Factory<HTFluidResult, R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("ingredient"),
        HTBasicSingleFluidRecipe::ingredient,
        HTFluidResult.CODEC.fieldOf("result"),
        HTBasicSingleFluidRecipe::result,
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
        RESULTS,
        HTItemWithCatalystRecipe::results,
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
