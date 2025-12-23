package hiiragi283.ragium.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.crafting.HTClearComponentRecipe
import hiiragi283.ragium.common.crafting.HTPotionDropRecipe
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
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

    @JvmField
    val ALLOYING: RecipeSerializer<HTAlloyingRecipe> = REGISTER.registerSerializer(
        RagiumConst.ALLOYING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC
                .listOf(2, 3)
                .fieldOf(HTConst.INGREDIENT)
                .forGetter(HTAlloyingRecipe::ingredients),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTAlloyingRecipe::result),
            HTItemResult.CODEC.optionalFieldOf("extra").forGetter(HTAlloyingRecipe::extra),
            ::HTAlloyingRecipe,
        ),
    )
}
