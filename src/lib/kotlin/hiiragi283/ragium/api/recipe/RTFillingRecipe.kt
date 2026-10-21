package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTFillingRecipe(
    itemIngredient: HTItemIngredient,
    fluidIngredient: HTFluidIngredient,
    result: HTItemResult,
    progressData: HTProgressData
) : HTItemAndFluidToRecipe.BasicItem(itemIngredient, fluidIngredient, result, progressData),
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTFillingRecipe> =
            RecipeSerializer(codec(::RTFillingRecipe), streamCodec(::RTFillingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTFillingRecipe> = RagiumRecipeSerializers.FILLING

    override fun getType(): RecipeType<RTFillingRecipe> = RagiumRecipeTypes.FILLING
}
