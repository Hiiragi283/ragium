package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemOrFluidToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTItemOrFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.util.Either
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTDryingRecipe(ingredient: Either<HTItemIngredient, HTFluidIngredient>, result: HTItemOrFluidResult, progressData: HTProgressData) :
    HTItemOrFluidToRecipe.BasicItemAndFluid(ingredient, result, progressData),
    HTSerializableRecipe<HTItemOrFluidRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTDryingRecipe> = RecipeSerializer(codec(::RTDryingRecipe), streamCodec(::RTDryingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTDryingRecipe> = RagiumRecipeSerializers.DRYING

    override fun getType(): RecipeType<RTDryingRecipe> = RagiumRecipeTypes.DRYING
}
