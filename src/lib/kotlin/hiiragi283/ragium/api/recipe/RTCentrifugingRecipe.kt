package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemSeparatingRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class RTCentrifugingRecipe(ingredient: HTItemIngredient, result: HTItemSeparatingRecipe.Result, progressData: HTProgressData) :
    HTItemSeparatingRecipe.Basic(ingredient, result, progressData),
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTCentrifugingRecipe> =
            RecipeSerializer(codec(::RTCentrifugingRecipe), streamCodec(::RTCentrifugingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTCentrifugingRecipe> = RagiumRecipeSerializers.CENTRIFUGING

    override fun getType(): RecipeType<RTCentrifugingRecipe> = RagiumRecipeTypes.CENTRIFUGING
}
