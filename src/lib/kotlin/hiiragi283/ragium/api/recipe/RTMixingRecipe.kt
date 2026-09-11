package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import net.minecraft.world.item.crafting.RecipeSerializer

class RTMixingRecipe(
    itemIngredient: HTItemIngredient,
    fluidIngredient: HTFluidIngredient,
    result: HTFluidResult,
    progressData: HTProgressData
) : HTItemAndFluidToRecipe.BasicFluid(itemIngredient, fluidIngredient, result, progressData),
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTMixingRecipe> =
            RecipeSerializer(codec(::RTMixingRecipe), streamCodec(::RTMixingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTMixingRecipe> = RagiumRecipeSerializers.MIXING

    override fun getType(): HTRecipeType<RTMixingRecipe> = RagiumRecipeTypes.MIXING
}
