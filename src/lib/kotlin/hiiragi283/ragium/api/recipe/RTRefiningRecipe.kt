package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTFluidToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import net.minecraft.world.item.crafting.RecipeSerializer

class RTRefiningRecipe(ingredient: HTFluidIngredient, result: HTItemOrFluidResult, progressData: HTProgressData) :
    HTFluidToRecipe.BasicItemAndFluid(ingredient, result, progressData),
    HTSerializableRecipe<HTSingleFluidRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTRefiningRecipe> =
            RecipeSerializer(codec(::RTRefiningRecipe), streamCodec(::RTRefiningRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTRefiningRecipe> = RagiumRecipeSerializers.REFINING

    override fun getType(): HTRecipeType<RTRefiningRecipe> = RagiumRecipeTypes.REFINING
}
