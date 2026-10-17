package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTFluidSeparatingRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.input.HTRecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTElectrolyzingRecipe(ingredient: HTFluidIngredient, result: Result, progressData: HTProgressData) :
    HTFluidSeparatingRecipe(ingredient, result, progressData),
    HTSerializableRecipe<HTRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTElectrolyzingRecipe> =
            RecipeSerializer(codec(::RTElectrolyzingRecipe), streamCodec(::RTElectrolyzingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTElectrolyzingRecipe> = RagiumRecipeSerializers.ELECTROLYZING

    override fun getType(): RecipeType<RTElectrolyzingRecipe> = RagiumRecipeTypes.ELECTROLYZING
}
