package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTCatalysedItemToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTItemWithCatalystRecipeInput
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTPressingRecipe(ingredient: HTItemIngredient, catalyst: HTItemCatalyst, result: HTItemResult, progressData: HTProgressData) :
    HTCatalysedItemToRecipe.BasicItem(ingredient, catalyst, result, progressData),
    HTSerializableRecipe<HTItemWithCatalystRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTPressingRecipe> =
            RecipeSerializer(codec(::RTPressingRecipe), streamCodec(::RTPressingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTPressingRecipe> = RagiumRecipeSerializers.PRESSING

    override fun getType(): RecipeType<RTPressingRecipe> = RagiumRecipeTypes.PRESSING
}
