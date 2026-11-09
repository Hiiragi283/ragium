package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

class RTPulverizingRecipe(ingredient: HTItemIngredient, result: HTItemResult, progressData: HTProgressData) :
    HTItemToRecipe.BasicItem(ingredient, result, progressData),
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTPulverizingRecipe> = RecipeSerializer(codec(::RTPulverizingRecipe), streamCodec(::RTPulverizingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTPulverizingRecipe> = RagiumRecipeSerializers.PULVERIZING

    override fun getType(): RecipeType<RTPulverizingRecipe> = RagiumRecipeTypes.PULVERIZING
}
