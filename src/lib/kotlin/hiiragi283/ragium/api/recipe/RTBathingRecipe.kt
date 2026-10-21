package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTBathingRecipe(
    itemIngredient: HTItemIngredient,
    fluidIngredient: HTFluidIngredient,
    result: HTItemResult,
    progressData: HTProgressData
) : HTItemAndFluidToRecipe.BasicItem(itemIngredient, fluidIngredient, result, progressData),
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTBathingRecipe> =
            RecipeSerializer(codec(::RTBathingRecipe), streamCodec(::RTBathingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTBathingRecipe> = RagiumRecipeSerializers.BATHING

    override fun getType(): RecipeType<RTBathingRecipe> = RagiumRecipeTypes.BATHING
}
