package hiiragi283.ragium.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.common.data.recipe.builder.HTItemToChancedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTItemToItemRecipeBuilder
import hiiragi283.core.common.recipe.base.HTBasicItemToChancedRecipe
import hiiragi283.core.common.recipe.base.HTBasicItemToItemRecipe
import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.crafting.HTBlueprintCloningRecipe
import hiiragi283.ragium.common.data.recipe.HTChemicalRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemAndItemRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTCanningRecipe
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTHolderEnchantingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.base.HTBasicItemAndItemRecipe
import hiiragi283.ragium.common.recipe.base.HTBasicItemOrFluidRecipe
import hiiragi283.ragium.common.recipe.base.HTChemicalIngredient
import hiiragi283.ragium.common.recipe.base.HTChemicalRecipe
import hiiragi283.ragium.common.recipe.base.HTChemicalResult
import hiiragi283.ragium.common.recipe.special.HTBookCloningRecipe
import hiiragi283.ragium.common.recipe.special.HTBucketDrainingRecipe
import hiiragi283.ragium.common.recipe.special.HTBucketFillingRecipe
import hiiragi283.ragium.common.recipe.special.HTPotionDrainingRecipe
import hiiragi283.ragium.common.recipe.special.HTPotionFillingRecipe
import hiiragi283.ragium.common.recipe.special.HTPrintingRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer

object RagiumRecipeSerializers {
    @JvmField
    val REGISTER = HTDeferredRecipeSerializerRegister(RagiumAPI.MOD_ID)

    //    Custom    //

    // Crafting
    @JvmField
    val BLUEPRINT_CLONING: SimpleCraftingRecipeSerializer<HTBlueprintCloningRecipe> = REGISTER.registerSerializer(
        "blueprint_cloning",
        SimpleCraftingRecipeSerializer(::HTBlueprintCloningRecipe),
    )

    // Printing
    @JvmField
    val BOOK_CLONING: RecipeSerializer<HTBookCloningRecipe> =
        REGISTER.registerSerializer("book_cloning", MapBiCodecs.unit(HTBookCloningRecipe))

    @JvmField
    val PRINTING: RecipeSerializer<HTPrintingRecipe> = REGISTER.registerSerializer(
        RagiumConst.PRINTING,
        MapBiCodec.composite(
            HTItemIngredient.UNSIZED_CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTPrintingRecipe::ingredient),
            HTItemHolderLike.HOLDER_CODEC.fieldOf("origin").forGetter(HTPrintingRecipe::origin),
            HTPrintingRecipe.CopyStrategy.CODEC
                .fieldOf("copy_strategy")
                .forGetter(HTPrintingRecipe::strategy),
            ::HTPrintingRecipe,
        ),
    )

    // Canning
    @JvmField
    val BUCKET_DRAINING: RecipeSerializer<HTBucketDrainingRecipe> =
        REGISTER.registerSerializer("bucket_draining", MapBiCodecs.unit(HTBucketDrainingRecipe))

    @JvmField
    val BUCKET_FILLING: RecipeSerializer<HTBucketFillingRecipe> =
        REGISTER.registerSerializer("bucket_filling", MapBiCodecs.unit(HTBucketFillingRecipe))

    @JvmField
    val POTION_DRAINING: RecipeSerializer<HTPotionDrainingRecipe> =
        REGISTER.registerSerializer("potion_draining", MapBiCodecs.unit(HTPotionDrainingRecipe))

    @JvmField
    val POTION_FILLING: RecipeSerializer<HTPotionFillingRecipe> =
        REGISTER.registerSerializer("potion_filling", MapBiCodecs.unit(HTPotionFillingRecipe))

    //    Machine    //

    @JvmStatic
    private fun <R : HTBasicItemToChancedRecipe> itemChanced(
        factory: HTItemToChancedRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTBasicItemToChancedRecipe::ingredient),
        HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTBasicItemToChancedRecipe::result),
        HTItemResult.CHANCED_CODEC.optionalFieldOf(HTConst.EXTRA_RESULT).forGetter(HTBasicItemToChancedRecipe::extraResult),
        HTProcessingRecipe.timeCodec(),
        factory::create,
    )

    @JvmStatic
    private fun <RECIPE : HTBasicItemOrFluidRecipe> itemOrFluid(
        factory: HTItemOrFluidRecipeBuilder.Factory<RECIPE>,
    ): MapBiCodec<RegistryFriendlyByteBuf, RECIPE> = MapBiCodec.composite(
        MapBiCodecs
            .ior(
                HTItemIngredient.CODEC.fieldOf(HTConst.ITEM_INGREDIENT),
                HTFluidIngredient.CODEC.fieldOf(HTConst.FLUID_INGREDIENT),
            ).forGetter(HTBasicItemOrFluidRecipe::ingredient),
        MapBiCodecs
            .ior(
                HTItemResult.CODEC.fieldOf(HTConst.ITEM_RESULT),
                HTFluidResult.CODEC.fieldOf(HTConst.FLUID_RESULT),
            ).forGetter(HTBasicItemOrFluidRecipe::result),
        HTProcessingRecipe.timeCodec(),
        factory::create,
    )

    @JvmStatic
    private fun <R : HTBasicItemToItemRecipe> itemToItem(
        factory: HTItemToItemRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTBasicItemToItemRecipe::ingredient),
        HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTBasicItemToItemRecipe::result),
        HTProcessingRecipe.timeCodec(),
        factory::create,
    )

    @JvmStatic
    private fun <R : HTBasicItemAndItemRecipe> itemAndItem(
        factory: HTItemAndItemRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("first_ingredient").forGetter(HTBasicItemAndItemRecipe::first),
        HTItemIngredient.CODEC.fieldOf("second_ingredient").forGetter(HTBasicItemAndItemRecipe::second),
        HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTBasicItemAndItemRecipe::result),
        HTProcessingRecipe.timeCodec(),
        factory::create,
    )

    // Machine - Basic

    @JvmField
    val ALLOYING: RecipeSerializer<HTAlloyingRecipe> =
        REGISTER.registerSerializer(
            RagiumConst.ALLOYING,
            MapBiCodec.composite(
                HTItemIngredient.CODEC
                    .listOf(2, 3)
                    .fieldOf(HTConst.INGREDIENT)
                    .forGetter(HTAlloyingRecipe::ingredients),
                HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTAlloyingRecipe::result),
                HTProcessingRecipe.timeCodec(),
                ::HTAlloyingRecipe,
            ),
        )

    @JvmField
    val COMPRESSING: RecipeSerializer<HTCompressingRecipe> =
        REGISTER.registerSerializer(RagiumConst.COMPRESSING, itemToItem(::HTCompressingRecipe))

    @JvmField
    val CUTTING: RecipeSerializer<HTCuttingRecipe> = REGISTER.registerSerializer(RagiumConst.CUTTING, itemChanced(::HTCuttingRecipe))

    @JvmField
    val PRESSING: RecipeSerializer<HTPressingRecipe> = REGISTER.registerSerializer(RagiumConst.PRESSING, itemAndItem(::HTPressingRecipe))

    // Machine - Heat
    @JvmField
    val MELTING: RecipeSerializer<HTMeltingRecipe> =
        REGISTER.registerSerializer(RagiumConst.MELTING, itemOrFluid(::HTMeltingRecipe))

    @JvmField
    val PYROLYZING: RecipeSerializer<HTPyrolyzingRecipe> =
        REGISTER.registerSerializer(RagiumConst.PYROLYZING, itemOrFluid(::HTPyrolyzingRecipe))

    @JvmField
    val REFINING: RecipeSerializer<HTRefiningRecipe> =
        REGISTER.registerSerializer(RagiumConst.REFINING, itemOrFluid(::HTRefiningRecipe))

    // Machine - Cool
    @JvmField
    val FREEZING: RecipeSerializer<HTFreezingRecipe> =
        REGISTER.registerSerializer(RagiumConst.FREEZING, itemOrFluid(::HTFreezingRecipe))

    // Machine - Chemical
    @JvmStatic
    private fun chemIng(maxItem: Int, maxFluid: Int): MapBiCodec<RegistryFriendlyByteBuf, HTChemicalIngredient> = MapBiCodecs
        .ior(
            HTItemIngredient.CODEC.listOf(0, maxItem).optionalFieldOf(HTConst.ITEM_INGREDIENT, listOf()),
            HTFluidIngredient.CODEC.listOf(0, maxFluid).optionalFieldOf(HTConst.FLUID_INGREDIENT, listOf()),
        )

    @JvmStatic
    private fun chemRes(maxItem: Int, maxFluid: Int): MapBiCodec<RegistryFriendlyByteBuf, HTChemicalResult> = MapBiCodecs
        .ior(
            HTItemResult.CODEC.listOrElement(0, maxItem).optionalFieldOf(HTConst.ITEM_RESULT, listOf()),
            HTFluidResult.CODEC.listOrElement(0, maxFluid).optionalFieldOf(HTConst.FLUID_RESULT, listOf()),
        )

    @JvmStatic
    private fun <RECIPE : HTChemicalRecipe> chemical(
        maxItemIn: Int,
        maxFluidIn: Int,
        maxItemOut: Int,
        maxFluidOut: Int,
        factory: HTChemicalRecipeBuilder.Factory<RECIPE>,
    ): MapBiCodec<RegistryFriendlyByteBuf, RECIPE> = MapBiCodec.composite(
        chemIng(maxItemIn, maxFluidIn).forGetter(HTChemicalRecipe::ingredients),
        chemRes(maxItemOut, maxFluidOut).forGetter(HTChemicalRecipe::results),
        HTProcessingRecipe.timeCodec(),
        factory::create,
    )

    @JvmField
    val CANNING: RecipeSerializer<HTCanningRecipe> =
        REGISTER.registerSerializer(RagiumConst.CANNING, itemOrFluid(::HTCanningRecipe))

    @JvmField
    val MIXING: RecipeSerializer<HTMixingRecipe> = REGISTER.registerSerializer(
        RagiumConst.MIXING,
        chemical(
            HTMixingRecipe.MAX_ITEM_INPUT,
            HTMixingRecipe.MAX_FLUID_INPUT,
            HTMixingRecipe.MAX_ITEM_OUTPUT,
            HTMixingRecipe.MAX_FLUID_OUTPUT,
            ::HTMixingRecipe,
        ),
    )

    @JvmField
    val WASHING: RecipeSerializer<HTWashingRecipe> = REGISTER.registerSerializer(
        RagiumConst.WASHING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(HTConst.ITEM_INGREDIENT).forGetter(HTWashingRecipe::itemIngredient),
            HTFluidIngredient.CODEC.fieldOf(HTConst.FLUID_INGREDIENT).forGetter(HTWashingRecipe::fluidIngredient),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTWashingRecipe::result),
            HTItemResult.CHANCED_CODEC.optionalFieldOf(HTConst.EXTRA_RESULT).forGetter(HTWashingRecipe::extraResult),
            HTProcessingRecipe.timeCodec(),
            ::HTWashingRecipe,
        ),
    )

    // Machine - Matter

    // Device
    @JvmField
    val HOLDER_ENCHANTING: RecipeSerializer<HTHolderEnchantingRecipe> = REGISTER.registerSerializer(
        "${RagiumConst.ENCHANTING}/holder",
        MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTHolderEnchantingRecipe::ingredient),
            VanillaBiCodecs.holder(Registries.ENCHANTMENT).fieldOf("enchantment").forGetter(HTHolderEnchantingRecipe::holder),
            ::HTHolderEnchantingRecipe,
        ),
    )

    @JvmField
    val PLANTING: RecipeSerializer<HTPlantingRecipe> = REGISTER.registerSerializer(
        RagiumConst.PLANTING,
        MapBiCodec.composite(
            HTItemHolderLike.HOLDER_CODEC.fieldOf("seed").forGetter(HTPlantingRecipe::seed),
            HTItemIngredient.UNSIZED_CODEC.fieldOf("soil").forGetter(HTPlantingRecipe::soil),
            HTItemResult.CODEC.fieldOf("crop").forGetter(HTPlantingRecipe::crop),
            HTProcessingRecipe.timeCodec(),
            ::HTPlantingRecipe,
        ),
    )
}
