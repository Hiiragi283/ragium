package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTFluidSeparatingRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.input.HTSingleFluidRecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTRefiningRecipe(ingredient: HTFluidIngredient, result: Result, progressData: HTProgressData) :
    HTFluidSeparatingRecipe(ingredient, result, progressData),
    HTSerializableRecipe<HTSingleFluidRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTRefiningRecipe> =
            RecipeSerializer(codec(::RTRefiningRecipe), streamCodec(::RTRefiningRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTRefiningRecipe> = RagiumRecipeSerializers.REFINING

    override fun getType(): RecipeType<RTRefiningRecipe> = RagiumRecipeTypes.REFINING
}
