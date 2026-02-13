package hiiragi283.ragium.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.crafting.HTPotionDropRecipe
import hiiragi283.ragium.common.data.recipe.HTChemicalRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemOrFluidRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTItemToChancedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTBendingRecipe
import hiiragi283.ragium.common.recipe.HTCanningRecipe
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTDistillingRecipe
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTLathingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.base.HTChemicalIngredient
import hiiragi283.ragium.common.recipe.base.HTChemicalRecipe
import hiiragi283.ragium.common.recipe.base.HTChemicalResult
import hiiragi283.ragium.common.recipe.base.HTItemOrFluidRecipe
import hiiragi283.ragium.common.recipe.base.HTItemToChancedRecipe
import hiiragi283.ragium.common.recipe.base.HTSingleProcessingRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import net.minecraft.world.item.enchantment.ItemEnchantments
import java.util.Optional

object RagiumRecipeSerializers {
    @JvmField
    val REGISTER = HTDeferredRecipeSerializerRegister(RagiumAPI.MOD_ID)

    //    Custom    //

    @JvmField
    val POTION_DROP: SimpleCraftingRecipeSerializer<HTPotionDropRecipe> =
        REGISTER.registerSerializer("potion_drop", SimpleCraftingRecipeSerializer(::HTPotionDropRecipe))

    //    Machine    //

    @JvmStatic
    private fun <R : HTSingleProcessingRecipe.ItemToItem> itemToItem(
        factory: HTSingleRecipeBuilder.Factory<HTItemIngredient, HTItemResult, R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTSingleProcessingRecipe.ItemToItem::ingredient),
        HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTSingleProcessingRecipe.ItemToItem::result),
        HTProcessingRecipe.SubParameters.CODEC.forGetter(HTSingleProcessingRecipe.ItemToItem::parameters),
        factory::create,
    )

    @JvmStatic
    private fun <R : HTItemToChancedRecipe> itemChanced(
        factory: HTItemToChancedRecipeBuilder.Factory<R>,
        max: Int,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTItemToChancedRecipe::ingredient),
        HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTItemToChancedRecipe::result),
        HTChancedItemResult.CODEC
            .listOrElement(0, max)
            .optionalFieldOf(HTConst.EXTRA_RESULT, listOf())
            .forGetter(HTItemToChancedRecipe::extraResults),
        HTProcessingRecipe.SubParameters.CODEC.forGetter(HTItemToChancedRecipe::parameters),
        factory::create,
    )

    // Machine - Basic
    @JvmField
    val ALLOYING: RecipeSerializer<HTAlloyingRecipe> = REGISTER.registerSerializer(
        RagiumConst.ALLOYING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC
                .listOf(2, 3)
                .fieldOf(HTConst.INGREDIENT)
                .forGetter(HTAlloyingRecipe::ingredients),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTAlloyingRecipe::result),
            HTChancedItemResult.CODEC
                .listOrElement(0, 1)
                .optionalFieldOf(HTConst.EXTRA_RESULT, listOf())
                .forGetter(HTAlloyingRecipe::extraResults),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTAlloyingRecipe::parameters),
            ::HTAlloyingRecipe,
        ),
    )

    @JvmField
    val BENDING: RecipeSerializer<HTBendingRecipe> = REGISTER.registerSerializer(RagiumConst.BENDING, itemToItem(::HTBendingRecipe))

    @JvmField
    val CRUSHING: RecipeSerializer<HTCrushingRecipe> = REGISTER.registerSerializer(RagiumConst.CRUSHING, itemChanced(::HTCrushingRecipe, 3))

    @JvmField
    val CUTTING: RecipeSerializer<HTCuttingRecipe> = REGISTER.registerSerializer(RagiumConst.CUTTING, itemChanced(::HTCuttingRecipe, 1))

    @JvmField
    val LATHING: RecipeSerializer<HTLathingRecipe> = REGISTER.registerSerializer(RagiumConst.LATHING, itemToItem(::HTLathingRecipe))

    @JvmField
    val PRESSING: RecipeSerializer<HTPressingRecipe> = REGISTER.registerSerializer(
        RagiumConst.PRESSING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTPressingRecipe::ingredient),
            HTItemIngredient.UNSIZED_CODEC.fieldOf(HTConst.CATALYST).forGetter(HTPressingRecipe::catalyst),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTPressingRecipe::result),
            BiCodec.BOOL.optionalFieldOf("copy_component", false).forGetter(HTPressingRecipe::copyComponent),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTPressingRecipe::parameters),
            ::HTPressingRecipe,
        ),
    )

    // Machine - Heat
    @JvmStatic
    private fun <RECIPE : HTItemOrFluidRecipe> itemOrFluid(
        factory: HTItemOrFluidRecipeBuilder.Factory<RECIPE>,
    ): MapBiCodec<RegistryFriendlyByteBuf, RECIPE> = MapBiCodec.composite(
        MapBiCodecs
            .ior(
                HTItemIngredient.CODEC.fieldOf(HTConst.ITEM_INGREDIENT),
                HTFluidIngredient.CODEC.fieldOf(HTConst.FLUID_INGREDIENT),
            ).forGetter(HTItemOrFluidRecipe::ingredient),
        MapBiCodecs
            .either(
                HTItemResult.CODEC.fieldOf(HTConst.ITEM_RESULT),
                HTFluidResult.CODEC.fieldOf(HTConst.FLUID_RESULT),
            ).forGetter(HTItemOrFluidRecipe::result),
        HTProcessingRecipe.SubParameters.CODEC.forGetter(HTItemOrFluidRecipe::parameters),
        factory::create,
    )

    @JvmField
    val DISTILLING: RecipeSerializer<HTDistillingRecipe> = REGISTER.registerSerializer(
        RagiumConst.DISTILLING,
        MapBiCodec.composite(
            HTFluidIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTDistillingRecipe::ingredient),
            chemRes(HTDistillingRecipe.MAX_ITEM_OUTPUT, HTDistillingRecipe.MAX_FLUID_OUTPUT).forGetter(HTDistillingRecipe::results),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTDistillingRecipe::parameters),
            ::HTDistillingRecipe,
        ),
    )

    @JvmField
    val MELTING: RecipeSerializer<HTMeltingRecipe> = REGISTER.registerSerializer(RagiumConst.MELTING, itemOrFluid(::HTMeltingRecipe))

    @JvmField
    val PYROLYZING: RecipeSerializer<HTPyrolyzingRecipe> = REGISTER.registerSerializer(
        RagiumConst.PYROLYZING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(HTConst.ITEM_INGREDIENT).forGetter(HTPyrolyzingRecipe::itemIngredient),
            HTFluidIngredient.CODEC.optionalFieldOf(HTConst.FLUID_INGREDIENT).forGetter { Optional.ofNullable(it.fluidIngredient) },
            HTItemResult.CODEC.fieldOf(HTConst.ITEM_RESULT).forGetter(HTPyrolyzingRecipe::itemResult),
            HTFluidResult.CODEC.fieldOf(HTConst.FLUID_RESULT).forGetter(HTPyrolyzingRecipe::fluidResult),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTPyrolyzingRecipe::parameters),
            ::HTPyrolyzingRecipe,
        ),
    )

    // Machine - Cool
    @JvmField
    val FREEZING: RecipeSerializer<HTFreezingRecipe> = REGISTER.registerSerializer(RagiumConst.FREEZING, itemOrFluid(::HTFreezingRecipe))

    @JvmField
    val SOLIDIFYING: RecipeSerializer<HTSolidifyingRecipe> = REGISTER.registerSerializer(
        RagiumConst.SOLIDIFYING,
        MapBiCodec.composite(
            HTFluidIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTSolidifyingRecipe::ingredient),
            HTItemIngredient.UNSIZED_CODEC.fieldOf(HTConst.CATALYST).forGetter(HTSolidifyingRecipe::catalyst),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTSolidifyingRecipe::result),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTSolidifyingRecipe::parameters),
            ::HTSolidifyingRecipe,
        ),
    )

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
        HTProcessingRecipe.SubParameters.CODEC.forGetter(HTChemicalRecipe::parameters),
        factory::create,
    )

    @JvmField
    val CANNING: RecipeSerializer<HTCanningRecipe> = REGISTER.registerSerializer(
        RagiumConst.CANNING,
        MapBiCodec.composite(
            HTFluidIngredient.CODEC.fieldOf(HTConst.FLUID_INGREDIENT).forGetter(HTCanningRecipe::fluidIngredient),
            HTItemIngredient.CODEC.fieldOf(HTConst.ITEM_INGREDIENT).forGetter(HTCanningRecipe::itemIngredient),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTCanningRecipe::result),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTCanningRecipe::parameters),
            ::HTCanningRecipe,
        ),
    )

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
            HTChancedItemResult.CODEC
                .listOrElement(0, 3)
                .optionalFieldOf(HTConst.EXTRA_RESULT, listOf())
                .forGetter(HTWashingRecipe::extraResults),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTWashingRecipe::parameters),
            ::HTWashingRecipe,
        ),
    )

    // Machine - Matter

    // Device
    @JvmField
    val COMPRESSING: RecipeSerializer<HTCompressingRecipe> = REGISTER.registerSerializer(
        RagiumConst.COMPRESSING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTCompressingRecipe::ingredient),
            BiCodecs.POSITIVE_INT.fieldOf("power").forGetter(HTCompressingRecipe::power),
            HTItemIngredient.UNSIZED_CODEC.optionalFieldOf(HTConst.CATALYST).forGetter { Optional.ofNullable(it.catalyst) },
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTCompressingRecipe::result),
            ::HTCompressingRecipe,
        ),
    )

    @JvmField
    val ENCHANTING: RecipeSerializer<HTEnchantingRecipe> = REGISTER.registerSerializer(
        RagiumConst.ENCHANTING,
        MapBiCodec.composite(
            HTItemIngredient.UNSIZED_CODEC.fieldOf("book").forGetter(HTEnchantingRecipe::book),
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTEnchantingRecipe::ingredient),
            BiCodecs
                .either(
                    VanillaBiCodecs.holder(Registries.ENCHANTMENT),
                    BiCodec.of(ItemEnchantments.CODEC, ItemEnchantments.STREAM_CODEC),
                    true,
                ).fieldOf("enchantment")
                .forGetter(HTEnchantingRecipe::contents),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTEnchantingRecipe::parameters),
            ::HTEnchantingRecipe,
        ),
    )

    @JvmField
    val PLANTING: RecipeSerializer<HTPlantingRecipe> = REGISTER.registerSerializer(
        RagiumConst.PLANTING,
        MapBiCodec.composite(
            HTItemHolderLike.HOLDER_CODEC.fieldOf("seed").forGetter(HTPlantingRecipe::seed),
            HTItemIngredient.UNSIZED_CODEC.fieldOf("soil").forGetter(HTPlantingRecipe::soil),
            HTItemResult.CODEC.fieldOf("crop").forGetter(HTPlantingRecipe::crop),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTPlantingRecipe::parameters),
            ::HTPlantingRecipe,
        ),
    )
}
