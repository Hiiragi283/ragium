package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SingleRecipeInput

class RTCompressingRecipe(ingredient: HTItemIngredient, result: HTItemResult, progressData: HTProgressData) :
    HTItemToItemRecipe.Basic(ingredient, result, progressData),
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTCompressingRecipe> = RecipeSerializer(codec(::RTCompressingRecipe), streamCodec(::RTCompressingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTCompressingRecipe> = RagiumRecipeSerializers.COMPRESSING

    override fun getType(): HTRecipeType<RTCompressingRecipe> = RagiumRecipeTypes.COMPRESSING
}
