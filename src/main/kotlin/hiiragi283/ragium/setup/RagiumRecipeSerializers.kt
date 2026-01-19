package hiiragi283.ragium.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.HTRecipeBiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.api.serialization.codec.ParameterCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.crafting.HTPotionDropRecipe
import hiiragi283.ragium.common.data.recipe.HTChancedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTBlockSimulatingRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTDryingRecipe
import hiiragi283.ragium.common.recipe.HTEnchantingRecipe
import hiiragi283.ragium.common.recipe.HTEntitySimulatingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import hiiragi283.ragium.common.recipe.HTPressingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
import hiiragi283.ragium.common.recipe.base.HTChancedRecipe
import hiiragi283.ragium.common.recipe.base.HTComplexResultRecipe
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import net.minecraft.world.item.enchantment.ItemEnchantments
import org.apache.commons.lang3.math.Fraction

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
    private fun <T1 : Any, T2 : Any, RECIPE : HTProcessingRecipe<*>> processing(
        codec1: ParameterCodec<in RegistryFriendlyByteBuf, RECIPE, T1>,
        codec2: ParameterCodec<in RegistryFriendlyByteBuf, RECIPE, T2>,
        factory: (T1, T2, Int, Fraction) -> RECIPE,
    ): MapBiCodec<RegistryFriendlyByteBuf, RECIPE> = MapBiCodec.composite(
        codec1,
        codec2,
        HTRecipeBiCodecs.TIME.forGetter(HTProcessingRecipe<*>::time),
        HTRecipeBiCodecs.EXP.forGetter(HTProcessingRecipe<*>::exp),
        factory,
    )

    @JvmStatic
    private fun <T1 : Any, T2 : Any, T3 : Any, RECIPE : HTProcessingRecipe<*>> processing(
        codec1: ParameterCodec<in RegistryFriendlyByteBuf, RECIPE, T1>,
        codec2: ParameterCodec<in RegistryFriendlyByteBuf, RECIPE, T2>,
        codec3: ParameterCodec<in RegistryFriendlyByteBuf, RECIPE, T3>,
        factory: (T1, T2, T3, Int, Fraction) -> RECIPE,
    ): MapBiCodec<RegistryFriendlyByteBuf, RECIPE> = MapBiCodec.composite(
        codec1,
        codec2,
        codec3,
        HTRecipeBiCodecs.TIME.forGetter(HTProcessingRecipe<*>::time),
        HTRecipeBiCodecs.EXP.forGetter(HTProcessingRecipe<*>::exp),
        factory,
    )

    @JvmStatic
    private fun <I : Any, R : HTComplexResultRecipe.Simple> complex(
        codec: ParameterCodec<in RegistryFriendlyByteBuf, R, I>,
        factory: HTComplexRecipeBuilder.Factory<I, R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = processing(
        codec,
        COMPLEX_RESULT.forGetter(HTComplexResultRecipe.Simple::result),
        factory::create,
    )

    @JvmStatic
    private fun <R : HTChancedRecipe> chanced(
        factory: HTChancedRecipeBuilder.Factory<R>,
        max: Int,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = processing(
        HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTChancedRecipe::ingredient),
        HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTChancedRecipe::result),
        HTChancedItemResult.CODEC
            .listOrElement(0, max)
            .optionalFieldOf(RagiumConst.EXTRA_RESULT, listOf())
            .forGetter(HTChancedRecipe::extraResults),
        factory::create,
    )

    @JvmField
    val ALLOYING: RecipeSerializer<HTAlloyingRecipe> = REGISTER.registerSerializer(
        RagiumConst.ALLOYING,
        processing(
            HTItemIngredient.CODEC
                .listOf(2, 3)
                .fieldOf(HTConst.INGREDIENT)
                .forGetter { recipe: HTAlloyingRecipe ->
                    listOfNotNull(recipe.firstIngredient, recipe.secondIngredient, recipe.thirdIngredient)
                },
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTAlloyingRecipe::result),
            ::HTAlloyingRecipe,
        ),
    )

    @JvmField
    val CRUSHING: RecipeSerializer<HTCrushingRecipe> = REGISTER.registerSerializer(RagiumConst.CRUSHING, chanced(::HTCrushingRecipe, 3))

    @JvmField
    val CUTTING: RecipeSerializer<HTCuttingRecipe> = REGISTER.registerSerializer(RagiumConst.CUTTING, chanced(::HTCuttingRecipe, 1))

    @JvmField
    val DRYING: RecipeSerializer<HTDryingRecipe> = REGISTER.registerSerializer(
        RagiumConst.DRYING,
        complex(
            BiCodecs
                .either(HTItemIngredient.CODEC, HTFluidIngredient.CODEC, true)
                .fieldOf(HTConst.INGREDIENT)
                .forGetter(HTDryingRecipe::ingredient),
            ::HTDryingRecipe,
        ),
    )

    @JvmField
    val ENCHANTING: RecipeSerializer<HTEnchantingRecipe> = REGISTER.registerSerializer(
        RagiumConst.ENCHANTING,
        processing(
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTEnchantingRecipe::ingredient),
            BiCodec
                .of(ItemEnchantments.CODEC, ItemEnchantments.STREAM_CODEC)
                .fieldOf("enchantment")
                .forGetter(HTEnchantingRecipe::enchantments),
            ::HTEnchantingRecipe,
        ),
    )

    @JvmField
    val MELTING: RecipeSerializer<HTMeltingRecipe> = REGISTER.registerSerializer(
        RagiumConst.MELTING,
        processing(
            HTItemIngredient.UNSIZED_CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTMeltingRecipe::ingredient),
            HTFluidResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTMeltingRecipe::result),
            ::HTMeltingRecipe,
        ),
    )

    @JvmField
    val MIXING: RecipeSerializer<HTMixingRecipe> = REGISTER.registerSerializer(
        RagiumConst.MIXING,
        complex(
            MapBiCodecs
                .pair(
                    HTItemIngredient.CODEC.fieldOf(RagiumConst.ITEM_INGREDIENT),
                    HTFluidIngredient.CODEC.fieldOf(RagiumConst.FLUID_INGREDIENT),
                ).forGetter(HTMixingRecipe::ingredient),
            ::HTMixingRecipe,
        ),
    )

    @JvmField
    val PLANTING: RecipeSerializer<HTPlantingRecipe> = REGISTER.registerSerializer(
        RagiumConst.PLANTING,
        MapBiCodec.composite(
            HTItemResourceType.CODEC.fieldOf("seed").forGetter(HTPlantingRecipe::seed),
            HTItemIngredient.UNSIZED_CODEC.fieldOf("soil").forGetter(HTPlantingRecipe::soil),
            HTItemResult.CODEC.fieldOf("crop").forGetter(HTPlantingRecipe::crop),
            HTRecipeBiCodecs.TIME.forGetter(HTPlantingRecipe::time),
            HTRecipeBiCodecs.EXP.forGetter(HTPlantingRecipe::exp),
            ::HTPlantingRecipe,
        ),
    )

    @JvmField
    val PRESSING: RecipeSerializer<HTPressingRecipe> = REGISTER.registerSerializer(
        RagiumConst.PRESSING,
        processing(
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTPressingRecipe::ingredient),
            HTItemIngredient.UNSIZED_CODEC.fieldOf(HTConst.CATALYST).forGetter(HTPressingRecipe::catalyst),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTPressingRecipe::result),
            ::HTPressingRecipe,
        ),
    )

    @JvmField
    val PYROLYZING: RecipeSerializer<HTPyrolyzingRecipe> = REGISTER.registerSerializer(
        RagiumConst.PYROLYZING,
        processing(
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTPyrolyzingRecipe::ingredient),
            HTItemResult.CODEC.fieldOf(HTConst.ITEM_RESULT).forGetter(HTPyrolyzingRecipe::itemResult),
            HTFluidResult.CODEC.fieldOf(HTConst.FLUID_RESULT).forGetter(HTPyrolyzingRecipe::fluidResult),
            ::HTPyrolyzingRecipe,
        ),
    )

    @JvmField
    val REFINING: RecipeSerializer<HTRefiningRecipe> = REGISTER.registerSerializer(
        RagiumConst.REFINING,
        processing(
            HTFluidIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTRefiningRecipe::ingredient),
            HTFluidResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTRefiningRecipe::result),
            COMPLEX_RESULT.forGetter(HTRefiningRecipe::extraResult),
            ::HTRefiningRecipe,
        ),
    )

    @JvmField
    val SIMULATING_BLOCK: RecipeSerializer<HTBlockSimulatingRecipe> = REGISTER.registerSerializer(
        RagiumConst.SIMULATING_BLOCK,
        processing(
            HTItemIngredient.CODEC.optionalFieldOf(HTConst.INGREDIENT).forGetter(HTBlockSimulatingRecipe::ingredient),
            VanillaBiCodecs.holderSet(Registries.BLOCK).fieldOf(HTConst.CATALYST).forGetter(HTBlockSimulatingRecipe::catalyst),
            COMPLEX_RESULT.forGetter(HTBlockSimulatingRecipe::result),
            ::HTBlockSimulatingRecipe,
        ),
    )

    @JvmField
    val SIMULATING_ENTITY: RecipeSerializer<HTEntitySimulatingRecipe> = REGISTER.registerSerializer(
        RagiumConst.SIMULATING_ENTITY,
        processing(
            HTItemIngredient.CODEC.optionalFieldOf(HTConst.INGREDIENT).forGetter(HTEntitySimulatingRecipe::ingredient),
            VanillaBiCodecs.holderSet(Registries.ENTITY_TYPE).fieldOf(HTConst.CATALYST).forGetter(HTEntitySimulatingRecipe::catalyst),
            COMPLEX_RESULT.forGetter(HTEntitySimulatingRecipe::result),
            ::HTEntitySimulatingRecipe,
        ),
    )

    @JvmField
    val SOLIDIFYING: RecipeSerializer<HTSolidifyingRecipe> = REGISTER.registerSerializer(
        RagiumConst.SOLIDIFYING,
        processing(
            HTFluidIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTSolidifyingRecipe::ingredient),
            HTItemIngredient.UNSIZED_CODEC.fieldOf(HTConst.CATALYST).forGetter(HTSolidifyingRecipe::catalyst),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTSolidifyingRecipe::result),
            ::HTSolidifyingRecipe,
        ),
    )
}
