package hiiragi283.ragium.api.recipe

import hiiragi283.lib.collection.Nel
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTSizedShapelessRecipe
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTAlloyingRecipe(ingredients: Nel<HTItemIngredient>, result: HTItemResult, progressData: HTProgressData) :
    HTSizedShapelessRecipe.Basic(ingredients, result, progressData),
    HTSerializableRecipe<RecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTAlloyingRecipe> =
            RecipeSerializer(codec(::RTAlloyingRecipe), streamCodec(::RTAlloyingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTAlloyingRecipe> = RagiumRecipeSerializers.ALLOYING

    override fun getType(): RecipeType<RTAlloyingRecipe> = RagiumRecipeTypes.ALLOYING
}
