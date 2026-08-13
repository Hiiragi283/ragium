package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SingleRecipeInput

class RTSmeltingRecipe(ingredient: HTItemIngredient, result: HTItemResult, progressData: HTProgressData) :
    HTItemToItemRecipe.Basic(ingredient, result, progressData),
    HTSerializableRecipe<SingleRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTSmeltingRecipe> = RecipeSerializer(codec(::RTSmeltingRecipe), streamCodec(::RTSmeltingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTSmeltingRecipe> = RagiumRecipeSerializers.SMELTING

    override fun getType(): HTRecipeType<RTSmeltingRecipe> = RagiumRecipeTypes.SMELTING
}
