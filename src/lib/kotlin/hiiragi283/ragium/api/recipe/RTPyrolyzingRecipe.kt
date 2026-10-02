package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class RTPyrolyzingRecipe(ingredient: HTItemIngredient, result: HTItemOrFluidResult, progressData: HTProgressData) :
    HTItemToRecipe.BasicItemAndFluid(ingredient, result, progressData),
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTPyrolyzingRecipe> =
            RecipeSerializer(codec(::RTPyrolyzingRecipe), streamCodec(::RTPyrolyzingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTPyrolyzingRecipe> = RagiumRecipeSerializers.PYROLYZING

    override fun getType(): RecipeType<RTPyrolyzingRecipe> = RagiumRecipeTypes.PYROLYZING
}
