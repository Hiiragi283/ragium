package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class RTTransformingRecipe(ingredient: HTItemIngredient, result: HTItemResult, progressData: HTProgressData) :
    HTItemToRecipe.BasicItem(ingredient, result, progressData),
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTTransformingRecipe> = RecipeSerializer(codec(::RTTransformingRecipe), streamCodec(::RTTransformingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTTransformingRecipe> = RagiumRecipeSerializers.TRANSFORMING

    override fun getType(): RecipeType<RTTransformingRecipe> = RagiumRecipeTypes.TRANSFORMING
}
