package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemToDoubleItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SingleRecipeInput
import java.util.Optional

class RTPlantingRecipe(
    ingredient: HTItemIngredient,
    primary: HTItemResult,
    secondary: Optional<HTItemResult>,
    progressData: HTProgressData
) : HTItemToDoubleItemRecipe.Basic(ingredient, primary, secondary, progressData),
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTPlantingRecipe> =
            RecipeSerializer(codec(::RTPlantingRecipe), streamCodec(::RTPlantingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTPlantingRecipe> = RagiumRecipeSerializers.PLANTING

    override fun getType(): HTRecipeType<RTPlantingRecipe> = RagiumRecipeTypes.PLANTING
}
