package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemOrFluidRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.Ior
import net.minecraft.world.item.crafting.RecipeSerializer

class RTBrewingRecipe(ingredient: Ior<HTItemIngredient, HTFluidIngredient>, result: Ior<HTItemResult, HTFluidResult>, progressData: HTProgressData) :
    HTItemOrFluidRecipe.Basic(ingredient, result, progressData),
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTBrewingRecipe> = RecipeSerializer(codec(::RTBrewingRecipe), streamCodec(::RTBrewingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTBrewingRecipe> = RagiumRecipeSerializers.BREWING

    override fun getType(): HTRecipeType<RTBrewingRecipe> = RagiumRecipeTypes.BREWING
}
