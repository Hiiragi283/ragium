package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemCombineRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTAssemblingRecipe(firstIngredient: HTItemIngredient, secondIngredient: HTItemIngredient, result: HTItemResult, progressData: HTProgressData) :
    HTItemCombineRecipe.Basic(firstIngredient, secondIngredient, result, progressData),
    HTSerializableRecipe<RecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTAssemblingRecipe> = RecipeSerializer(codec(::RTAssemblingRecipe), streamCodec(::RTAssemblingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTAssemblingRecipe> = RagiumRecipeSerializers.ASSEMBLING

    override fun getType(): RecipeType<RTAssemblingRecipe> = RagiumRecipeTypes.ASSEMBLING
}
