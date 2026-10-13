package hiiragi283.ragium.api.recipe

import hiiragi283.lib.collection.Nel
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemToDoubleItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTRecipeInput
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTCuttingRecipe(ingredient: HTItemIngredient, result: Nel<HTItemResult>, progressData: HTProgressData) :
    HTItemToDoubleItemRecipe.Basic(ingredient, result, progressData),
    HTSerializableRecipe<HTRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTCuttingRecipe> =
            RecipeSerializer(codec(::RTCuttingRecipe), streamCodec(::RTCuttingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTCuttingRecipe> = RagiumRecipeSerializers.CUTTING

    override fun getType(): RecipeType<RTCuttingRecipe> = RagiumRecipeTypes.CUTTING
}
