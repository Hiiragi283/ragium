package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer

class RTAlloyingRecipe(
    primary: HTItemIngredient,
    secondary: HTItemIngredient,
    result: HTItemResult,
    progressData: HTProgressData
) : HTDoubleItemToItemRecipe.Basic(primary, secondary, result, progressData),
    HTSerializableRecipe<RecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTAlloyingRecipe> =
            RecipeSerializer(codec(::RTAlloyingRecipe), streamCodec(::RTAlloyingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTAlloyingRecipe> = RagiumRecipeSerializers.ALLOYING

    override fun getType(): HTRecipeType<RTAlloyingRecipe> = RagiumRecipeTypes.ALLOYING
}
