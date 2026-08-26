package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTCatalystOrIngredient
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer

class RTFillingRecipe(itemIngredient: HTCatalystOrIngredient, fluidIngredient: HTFluidIngredient, result: HTItemResult, progressData: HTProgressData) :
    HTItemAndFluidToRecipe.BasicItem(itemIngredient, fluidIngredient, result, progressData),
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTFillingRecipe> = RecipeSerializer(codec(::RTFillingRecipe), streamCodec(::RTFillingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTFillingRecipe> = RagiumRecipeSerializers.FILLING

    override fun getType(): HTRecipeType<RTFillingRecipe> = RagiumRecipeTypes.FILLING
}
