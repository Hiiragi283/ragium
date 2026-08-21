package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemToItemAndFluidRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SingleRecipeInput

class RTPyrolyzingRecipe(ingredient: HTItemIngredient, itemResult: HTItemResult, fluidResult: HTFluidResult, progressData: HTProgressData) :
    HTItemToItemAndFluidRecipe.Basic(ingredient, itemResult, fluidResult, progressData),
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTPyrolyzingRecipe> = RecipeSerializer(codec(::RTPyrolyzingRecipe), streamCodec(::RTPyrolyzingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTPyrolyzingRecipe> = RagiumRecipeSerializers.PYROLYZING

    override fun getType(): HTRecipeType<RTPyrolyzingRecipe> = RagiumRecipeTypes.PYROLYZING
}
