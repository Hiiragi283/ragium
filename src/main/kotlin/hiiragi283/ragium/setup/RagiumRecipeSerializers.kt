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
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.crafting.HTPotionDropRecipe
import hiiragi283.ragium.common.data.recipe.HTChancedRecipeBuilder
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.base.HTItemToChancedRecipe
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import net.minecraft.world.item.enchantment.ItemEnchantments

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
            HTItemResult.CODEC.optionalFieldOf(HTConst.ITEM_RESULT),
            HTFluidResult.CODEC.optionalFieldOf(HTConst.FLUID_RESULT),
        )

    @JvmStatic
    private fun <R : HTItemToChancedRecipe> itemChanced(
        factory: HTChancedRecipeBuilder.Factory<HTItemIngredient, R>,
        max: Int,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTItemToChancedRecipe::ingredient),
        HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTItemToChancedRecipe::result),
        HTChancedItemResult.CODEC
            .listOrElement(0, max)
            .optionalFieldOf(RagiumConst.EXTRA_RESULT, listOf())
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
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTAlloyingRecipe::parameters),
            ::HTAlloyingRecipe,
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
    @JvmField
    val MIXING: RecipeSerializer<HTMixingRecipe> = REGISTER.registerSerializer(
        RagiumConst.MIXING,
        MapBiCodec.composite(
            MapBiCodecs
                .ior(
                    HTItemIngredient.CODEC.listOrElement(0, HTMixingRecipe.MAX_ITEM_INPUT).optionalFieldOf(RagiumConst.ITEM_INGREDIENT),
                    HTFluidIngredient.CODEC.listOrElement(0, HTMixingRecipe.MAX_FLUID_INPUT).optionalFieldOf(RagiumConst.FLUID_INGREDIENT),
                ).forGetter(HTMixingRecipe::ingredients),
            COMPLEX_RESULT.forGetter(HTMixingRecipe::result),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTMixingRecipe::parameters),
            ::HTMixingRecipe,
        ),
    )

    @JvmField
    val WASHING: RecipeSerializer<HTWashingRecipe> = REGISTER.registerSerializer(
        RagiumConst.WASHING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(RagiumConst.ITEM_INGREDIENT).forGetter(HTWashingRecipe::itemIngredient),
            HTFluidIngredient.CODEC.fieldOf(RagiumConst.FLUID_INGREDIENT).forGetter(HTWashingRecipe::fluidIngredient),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTWashingRecipe::result),
            HTChancedItemResult.CODEC
                .listOrElement(0, 3)
                .optionalFieldOf(RagiumConst.EXTRA_RESULT, listOf())
                .forGetter(HTWashingRecipe::extraResults),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTWashingRecipe::parameters),
            ::HTWashingRecipe,
        ),
    )

    // Machine - Matter

    // Device - Enchanting
    @JvmField
    val ENCHANTING: RecipeSerializer<HTEnchantingRecipe> = REGISTER.registerSerializer(
        RagiumConst.ENCHANTING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTEnchantingRecipe::ingredient),
            BiCodec
                .of(ItemEnchantments.CODEC, ItemEnchantments.STREAM_CODEC)
                .fieldOf("enchantment")
                .forGetter(HTEnchantingRecipe::enchantments),
            HTProcessingRecipe.SubParameters.CODEC.forGetter(HTEnchantingRecipe::parameters),
            ::HTEnchantingRecipe,
        ),
    )
}
