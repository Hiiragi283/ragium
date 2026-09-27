package hiiragi283.ragium.api.recipe

import hiiragi283.lib.collection.Nel
import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTFluidToDoubleFluidRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import net.minecraft.world.item.crafting.RecipeSerializer

class RTCentrifugingRecipe(ingredient: HTFluidIngredient, result: Nel<HTFluidResult>, progressData: HTProgressData) :
    HTFluidToDoubleFluidRecipe.Basic(ingredient, result, progressData),
    HTSerializableRecipe<HTSingleFluidRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTCentrifugingRecipe> =
            RecipeSerializer(codec(::RTCentrifugingRecipe), streamCodec(::RTCentrifugingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTCentrifugingRecipe> = RagiumRecipeSerializers.CENTRIFUGING

    override fun getType(): HTRecipeType<RTCentrifugingRecipe> = RagiumRecipeTypes.CENTRIFUGING
}
