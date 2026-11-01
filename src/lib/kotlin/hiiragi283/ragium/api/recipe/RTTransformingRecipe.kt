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

class RTTransformingRecipe(itemIngredient: HTItemIngredient, fluidIngredient: HTFluidIngredient, result: HTItemResult, progressData: HTProgressData) :
    HTItemAndFluidToRecipe.BasicItem(itemIngredient, fluidIngredient, result, progressData),
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTTransformingRecipe> = RecipeSerializer(codec(::RTTransformingRecipe), streamCodec(::RTTransformingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTTransformingRecipe> = RagiumRecipeSerializers.TRANSFORMING

    override fun getType(): RecipeType<RTTransformingRecipe> = RagiumRecipeTypes.TRANSFORMING
}
