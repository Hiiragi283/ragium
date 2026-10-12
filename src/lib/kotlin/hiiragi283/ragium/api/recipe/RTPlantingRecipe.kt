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

class RTPlantingRecipe(ingredient: HTItemIngredient, result: Nel<HTItemResult>, progressData: HTProgressData) :
    HTItemToDoubleItemRecipe.Basic(ingredient, result, progressData),
    HTSerializableRecipe<HTRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTPlantingRecipe> =
            RecipeSerializer(codec(::RTPlantingRecipe), streamCodec(::RTPlantingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTPlantingRecipe> = RagiumRecipeSerializers.PLANTING

    override fun getType(): RecipeType<RTPlantingRecipe> = RagiumRecipeTypes.PLANTING
}
