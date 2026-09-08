package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTFluidToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer

class RTFreezingRecipe(ingredient: HTFluidIngredient, result: HTItemResult, progressData: HTProgressData) :
    HTFluidToRecipe.BasicItem(ingredient, result, progressData),
    HTSerializableRecipe<HTSingleFluidRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTFreezingRecipe> =
            RecipeSerializer(codec(::RTFreezingRecipe), streamCodec(::RTFreezingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTFreezingRecipe> = RagiumRecipeSerializers.FREEZING

    override fun getType(): HTRecipeType<RTFreezingRecipe> = RagiumRecipeTypes.FREEZING
}
