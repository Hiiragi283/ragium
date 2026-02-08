package hiiragi283.ragium.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTComplexResult
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
import hiiragi283.ragium.common.data.recipe.HTItemToChancedRecipeBuilder
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import hiiragi283.ragium.common.recipe.HTCompressingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTReactingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.base.HTChemicalIngredient
import hiiragi283.ragium.common.recipe.base.HTChemicalResult
import hiiragi283.ragium.common.recipe.base.HTItemToChancedRecipe
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
    private val COMPLEX_RESULT: MapBiCodec<RegistryFriendlyByteBuf, HTComplexResult> = MapBiCodecs
        .ior(
            HTItemResult.CODEC.fieldOf(HTConst.ITEM_RESULT),
            HTFluidResult.CODEC.fieldOf(HTConst.FLUID_RESULT),
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
    val ASSEMBLING: RecipeSerializer<HTAssemblingRecipe> = REGISTER.registerSerializer(
        RagiumConst.ASSEMBLING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC
                .listOf(1, HTAssemblingRecipe.MAX_ITEM_INPUTS)
                .fieldOf(HTConst.INGREDIENT)
                .forGetter(HTAssemblingRecipe::itemIngredients),
            HTFluidIngredient.CODEC
                .optionalFieldOf(HTConst.FLUID_INGREDIENT)
                .forGetter { Optional.ofNullable(it.fluidIngredient) },
            BiCodecs.NON_NEGATIVE_INT.fieldOf("circuit").forGetter(HTAssemblingRecipe::circuit),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTAssemblingRecipe::result),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTAssemblingRecipe::parameters),
            ::HTAssemblingRecipe,
        ),
    )

    @JvmField
    val CRUSHING: RecipeSerializer<HTCrushingRecipe> = REGISTER.registerSerializer(RagiumConst.CRUSHING, itemChanced(::HTCrushingRecipe, 3))

    @JvmField
    val CUTTING: RecipeSerializer<HTCuttingRecipe> = REGISTER.registerSerializer(RagiumConst.CUTTING, itemChanced(::HTCuttingRecipe, 1))

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
    @JvmField
    val MELTING: RecipeSerializer<HTMeltingRecipe> = REGISTER.registerSerializer(
        RagiumConst.MELTING,
        MapBiCodec.composite(
            HTItemIngredient.UNSIZED_CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTMeltingRecipe::ingredient),
            HTFluidResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTMeltingRecipe::result),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTMeltingRecipe::parameters),
            ::HTMeltingRecipe,
        ),
    )

    @JvmField
    val PYROLYZING: RecipeSerializer<HTPyrolyzingRecipe> = REGISTER.registerSerializer(
        RagiumConst.PYROLYZING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTPyrolyzingRecipe::ingredient),
            HTItemResult.CODEC.fieldOf(HTConst.ITEM_RESULT).forGetter(HTPyrolyzingRecipe::itemResult),
            HTFluidResult.CODEC.fieldOf(HTConst.FLUID_RESULT).forGetter(HTPyrolyzingRecipe::fluidResult),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTPyrolyzingRecipe::parameters),
            ::HTPyrolyzingRecipe,
        ),
    )

    @JvmField
    val REFINING: RecipeSerializer<HTRefiningRecipe> = REGISTER.registerSerializer(
        RagiumConst.REFINING,
        MapBiCodec.composite(
            HTFluidIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTRefiningRecipe::ingredient),
            HTFluidResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTRefiningRecipe::result),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTRefiningRecipe::parameters),
            ::HTRefiningRecipe,
        ),
    )

    @JvmField
    val SOLIDIFYING: RecipeSerializer<HTSolidifyingRecipe> = REGISTER.registerSerializer(
        RagiumConst.SOLIDIFYING,
        MapBiCodec.composite(
            HTFluidIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTSolidifyingRecipe::fluidIngredient),
            HTItemIngredient.UNSIZED_CODEC.fieldOf(HTConst.CATALYST).forGetter(HTSolidifyingRecipe::itemIngredient),
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

    @JvmField
    val MIXING: RecipeSerializer<HTMixingRecipe> = REGISTER.registerSerializer(
        RagiumConst.MIXING,
        MapBiCodec.composite(
            chemIng(HTMixingRecipe.MAX_ITEM_INPUT, HTMixingRecipe.MAX_FLUID_INPUT).forGetter(HTMixingRecipe::ingredients),
            COMPLEX_RESULT.forGetter(HTMixingRecipe::result),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTMixingRecipe::parameters),
            ::HTMixingRecipe,
        ),
    )

    @JvmField
    val REACTING: RecipeSerializer<HTReactingRecipe> = REGISTER.registerSerializer(
        RagiumConst.REACTING,
        MapBiCodec.composite(
            chemIng(HTReactingRecipe.MAX_ITEM_INPUT, HTReactingRecipe.MAX_FLUID_INPUT).forGetter(HTReactingRecipe::ingredients),
            HTItemIngredient.UNSIZED_CODEC.optionalFieldOf(HTConst.CATALYST).forGetter { Optional.ofNullable(it.catalyst) },
            chemRes(HTReactingRecipe.MAX_ITEM_OUTPUT, HTReactingRecipe.MAX_FLUID_OUTPUT).forGetter(HTReactingRecipe::results),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTReactingRecipe::parameters),
            ::HTReactingRecipe,
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
