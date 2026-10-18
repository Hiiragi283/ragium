package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTPressingRecipe(
    ingredient: HTItemIngredient,
    catalyst: HTItemCatalyst,
    result: HTItemResult,
    progressData: HTProgressData
) : HTDoubleItemToItemRecipe.Catalysed(ingredient, catalyst, result, progressData),
    HTSerializableRecipe<RecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTPressingRecipe> =
            RecipeSerializer(codec(::RTPressingRecipe), streamCodec(::RTPressingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTPressingRecipe> = RagiumRecipeSerializers.PRESSING

    override fun getType(): RecipeType<RTPressingRecipe> = RagiumRecipeTypes.PRESSING
}
