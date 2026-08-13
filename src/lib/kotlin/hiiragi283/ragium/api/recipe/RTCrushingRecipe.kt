package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemToDoubleItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.Option
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SingleRecipeInput

class RTCrushingRecipe(ingredient: HTItemIngredient, primary: HTItemResult, secondary: Option<HTItemResult>, progressData: HTProgressData) :
    HTItemToDoubleItemRecipe.Basic(ingredient, primary, secondary, progressData),
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTCrushingRecipe> = RecipeSerializer(codec(::RTCrushingRecipe), streamCodec(::RTCrushingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTCrushingRecipe> = RagiumRecipeSerializers.CRUSHING

    override fun getType(): HTRecipeType<RTCrushingRecipe> = RagiumRecipeTypes.CRUSHING
}
