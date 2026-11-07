package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTFluidToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTDryingRecipe(ingredient: HTFluidIngredient, result: HTItemOrFluidResult, progressData: HTProgressData) :
    HTFluidToRecipe.BasicItemAndFluid(ingredient, result, progressData),
    HTSerializableRecipe<HTSingleFluidRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTDryingRecipe> = RecipeSerializer(codec(::RTDryingRecipe), streamCodec(::RTDryingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTDryingRecipe> = RagiumRecipeSerializers.DRYING

    override fun getType(): RecipeType<RTDryingRecipe> = RagiumRecipeTypes.DRYING
}
