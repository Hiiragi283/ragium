package hiiragi283.ragium.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.HTRecipeBiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.api.serialization.codec.ParameterCodec
import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.crafting.HTClearComponentRecipe
import hiiragi283.ragium.common.crafting.HTPotionDropRecipe
import hiiragi283.ragium.common.data.recipe.HTChancedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTChancedRecipe
import hiiragi283.ragium.common.recipe.HTComplexRecipe
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTDryingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import org.apache.commons.lang3.math.Fraction

object RagiumRecipeSerializers {
    @JvmField
    val REGISTER = HTDeferredRecipeSerializerRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <RECIPE : Recipe<*>> register(name: String, serializer: RecipeSerializer<RECIPE>): RecipeSerializer<RECIPE> {
        REGISTER.register(name) { _: ResourceLocation -> serializer }
        return serializer
    }

    //    Custom    //

    @JvmField
    val CLEAR_COMPONENT: RecipeSerializer<HTClearComponentRecipe> =
        REGISTER.registerSerializer("clear_component", HTClearComponentRecipe.CODEC)

    @JvmField
    val POTION_DROP: RecipeSerializer<HTPotionDropRecipe> =
        register("potion_drop", SimpleCraftingRecipeSerializer(::HTPotionDropRecipe))

    //    Machine    //

    @JvmStatic
    private val COMPLEX_RESULT: MapBiCodec<RegistryFriendlyByteBuf, HTComplexResult> = MapBiCodecs
        .ior(
            HTItemResult.CODEC.optionalFieldOf(HTConst.ITEM_RESULT),
            HTFluidResult.CODEC.optionalFieldOf(HTConst.FLUID_RESULT),
        )

    @JvmStatic
    private fun <T1 : Any, T2 : Any, RECIPE : HTProcessingRecipe> processing(
        codec1: ParameterCodec<in RegistryFriendlyByteBuf, RECIPE, T1>,
        codec2: ParameterCodec<in RegistryFriendlyByteBuf, RECIPE, T2>,
        factory: (T1, T2, Int, Fraction) -> RECIPE,
    ): MapBiCodec<RegistryFriendlyByteBuf, RECIPE> = MapBiCodec.composite(
        codec1,
        codec2,
        HTRecipeBiCodecs.TIME.forGetter(HTProcessingRecipe::time),
        HTRecipeBiCodecs.EXP.forGetter(HTProcessingRecipe::exp),
        factory,
    )

    @JvmStatic
    private fun <T1 : Any, T2 : Any, T3 : Any, RECIPE : HTProcessingRecipe> processing(
        codec1: ParameterCodec<in RegistryFriendlyByteBuf, RECIPE, T1>,
        codec2: ParameterCodec<in RegistryFriendlyByteBuf, RECIPE, T2>,
        codec3: ParameterCodec<in RegistryFriendlyByteBuf, RECIPE, T3>,
        factory: (T1, T2, T3, Int, Fraction) -> RECIPE,
    ): MapBiCodec<RegistryFriendlyByteBuf, RECIPE> = MapBiCodec.composite(
        codec1,
        codec2,
        codec3,
        HTRecipeBiCodecs.TIME.forGetter(HTProcessingRecipe::time),
        HTRecipeBiCodecs.EXP.forGetter(HTProcessingRecipe::exp),
        factory,
    )

    @JvmStatic
    private fun <R : HTComplexRecipe> complex(factory: HTComplexRecipeBuilder.Factory<R>): MapBiCodec<RegistryFriendlyByteBuf, R> =
        processing(
            BiCodecs
                .either(HTItemIngredient.CODEC, HTFluidIngredient.CODEC)
                .fieldOf(HTConst.INGREDIENT)
                .forGetter(HTComplexRecipe::ingredient),
            COMPLEX_RESULT.forGetter(HTComplexRecipe::result),
            factory::create,
        )

    @JvmStatic
    private fun <R : HTChancedRecipe> chanced(
        factory: HTChancedRecipeBuilder.Factory<R>,
        min: Int,
        max: Int,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = processing(
        HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTChancedRecipe::ingredient),
        HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTChancedRecipe::result),
        HTChancedItemResult.CODEC
            .listOrElement(min, max)
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
    val CRUSHING: RecipeSerializer<HTCrushingRecipe> = REGISTER.registerSerializer(RagiumConst.CRUSHING, chanced(::HTCrushingRecipe, 0, 3))

    @JvmField
    val DRYING: RecipeSerializer<HTDryingRecipe> =
        REGISTER.registerSerializer(RagiumConst.DRYING, complex(::HTDryingRecipe))

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
    val PYROLYZING: RecipeSerializer<HTPyrolyzingRecipe> =
        REGISTER.registerSerializer(RagiumConst.PYROLYZING, complex(::HTPyrolyzingRecipe))

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
}
