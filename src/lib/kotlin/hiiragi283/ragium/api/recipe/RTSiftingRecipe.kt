package hiiragi283.ragium.api.recipe

import hiiragi283.lib.collection.Nel
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemToMultiItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class RTSiftingRecipe(ingredient: HTItemIngredient, result: Nel<HTItemResult>, progressData: HTProgressData) :
    HTItemToMultiItemRecipe.Basic(ingredient, result, progressData),
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTSiftingRecipe> =
            RecipeSerializer(codec(::RTSiftingRecipe), streamCodec(::RTSiftingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTSiftingRecipe> = RagiumRecipeSerializers.SIFTING

    override fun getType(): RecipeType<RTSiftingRecipe> = RagiumRecipeTypes.SIFTING
}
