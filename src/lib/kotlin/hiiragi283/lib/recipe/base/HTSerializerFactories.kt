package hiiragi283.lib.recipe.base

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.recipe.HTItemToFluidRecipeBuilder
import hiiragi283.lib.data.recipe.HTItemToItemRecipeBuilder
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer

data object HTSerializerFactories {
    @JvmStatic
    fun <RECIPE> itemToItem(factory: HTItemToItemRecipeBuilder.Factory<RECIPE>): RecipeSerializer<RECIPE> where RECIPE : HTItemToItemRecipe.Basic, RECIPE : Recipe<*> = RecipeSerializer(
        HTCodecs.recordMap { instance ->
            instance.group(
                HTItemIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(HTItemToItemRecipe.Basic::ingredient),
                HTItemResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(HTItemToItemRecipe.Basic::result),
                HTProgressData.CODEC.forGetter(HTItemToItemRecipe.Basic::progressData),
            ).apply(instance, factory::create)
        },
        StreamCodec.composite(
            HTItemIngredient.STREAM_CODEC,
            HTItemToItemRecipe.Basic::ingredient,
            HTItemResult.STREAM_CODEC,
            HTItemToItemRecipe.Basic::result,
            HTProgressData.STREAM_CODEC,
            HTItemToItemRecipe.Basic::progressData,
            factory::create,
        ),
    )

    @JvmStatic
    fun <RECIPE> itemToFluid(factory: HTItemToFluidRecipeBuilder.Factory<RECIPE>): RecipeSerializer<RECIPE> where RECIPE : HTItemToFluidRecipe.Basic, RECIPE : Recipe<*> = RecipeSerializer(
        HTCodecs.recordMap { instance ->
            instance.group(
                HTItemIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(HTItemToFluidRecipe.Basic::ingredient),
                HTFluidResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(HTItemToFluidRecipe.Basic::result),
                HTProgressData.CODEC.forGetter(HTItemToFluidRecipe.Basic::progressData),
            ).apply(instance, factory::create)
        },
        StreamCodec.composite(
            HTItemIngredient.STREAM_CODEC,
            HTItemToFluidRecipe.Basic::ingredient,
            HTFluidResult.STREAM_CODEC,
            HTItemToFluidRecipe.Basic::result,
            HTProgressData.STREAM_CODEC,
            HTItemToFluidRecipe.Basic::progressData,
            factory::create,
        ),
    )
}
