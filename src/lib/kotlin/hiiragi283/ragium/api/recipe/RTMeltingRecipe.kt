package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemToFluidRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SingleRecipeInput

class RTMeltingRecipe(ingredient: HTItemIngredient, result: HTFluidResult, progressData: HTProgressData) :
    HTItemToFluidRecipe.Basic(ingredient, result, progressData),
    HTSerializableRecipe<SingleRecipeInput> {
    override fun getSerializer(): RecipeSerializer<RTMeltingRecipe> = RagiumRecipeSerializers.MELTING

    override fun getType(): HTRecipeType<RTMeltingRecipe> = RagiumRecipeTypes.MELTING
}
