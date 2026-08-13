package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer

class RTAssemblingRecipe(primary: HTItemIngredient, secondary: HTItemIngredient, result: HTItemResult, progressData: HTProgressData) :
    HTDoubleItemToItemRecipe.Basic(primary, secondary, result, progressData),
    HTSerializableRecipe<RecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTAssemblingRecipe> = RecipeSerializer(codec(::RTAssemblingRecipe), streamCodec(::RTAssemblingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTAssemblingRecipe> = RagiumRecipeSerializers.ASSEMBLING

    override fun getType(): HTRecipeType<RTAssemblingRecipe> = RagiumRecipeTypes.ASSEMBLING
}
