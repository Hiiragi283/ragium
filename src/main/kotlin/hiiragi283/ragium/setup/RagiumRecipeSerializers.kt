package hiiragi283.ragium.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.HTRecipeBiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.crafting.HTClearComponentRecipe
import hiiragi283.ragium.common.crafting.HTPotionDropRecipe
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTDryingRecipe
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer

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

    @JvmField
    val ALLOYING: RecipeSerializer<HTAlloyingRecipe> = REGISTER.registerSerializer(
        RagiumConst.ALLOYING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC
                .listOf(2, 3)
                .fieldOf(HTConst.INGREDIENT)
                .forGetter { recipe: HTAlloyingRecipe ->
                    listOfNotNull(recipe.firstIngredient, recipe.secondIngredient, recipe.thirdIngredient)
                },
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTAlloyingRecipe::result),
            HTRecipeBiCodecs.TIME.forGetter(HTAlloyingRecipe::time),
            HTRecipeBiCodecs.EXP.forGetter(HTAlloyingRecipe::exp),
            ::HTAlloyingRecipe,
        ),
    )

    @JvmField
    val DRYING: RecipeSerializer<HTDryingRecipe> = REGISTER.registerSerializer(
        RagiumConst.DRYING,
        MapBiCodec.composite(
            BiCodecs
                .either(
                    HTItemIngredient.CODEC,
                    HTFluidIngredient.CODEC,
                ).fieldOf(HTConst.INGREDIENT)
                .forGetter(HTDryingRecipe::ingredient),
            MapBiCodecs
                .ior(
                    HTItemResult.CODEC.optionalFieldOf(HTConst.ITEM_RESULT),
                    HTFluidResult.CODEC.optionalFieldOf(HTConst.FLUID_RESULT),
                ).forGetter(HTDryingRecipe::result),
            HTRecipeBiCodecs.TIME.forGetter(HTDryingRecipe::time),
            HTRecipeBiCodecs.EXP.forGetter(HTDryingRecipe::exp),
            ::HTDryingRecipe,
        ),
    )
}
