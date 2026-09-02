package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTCatalystOrIngredient
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import net.minecraft.world.item.crafting.RecipeSerializer

class RTBrewingRecipe(
    itemIngredient: HTCatalystOrIngredient,
    fluidIngredient: HTFluidIngredient,
    result: HTFluidResult,
    progressData: HTProgressData
) : HTItemAndFluidToRecipe.BasicFluid(itemIngredient, fluidIngredient, result, progressData),
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTBrewingRecipe> =
            RecipeSerializer(codec(::RTBrewingRecipe), streamCodec(::RTBrewingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTBrewingRecipe> = RagiumRecipeSerializers.BREWING

    override fun getType(): HTRecipeType<RTBrewingRecipe> = RagiumRecipeTypes.BREWING
}
