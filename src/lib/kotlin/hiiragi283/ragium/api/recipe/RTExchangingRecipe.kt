package hiiragi283.ragium.api.recipe

import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTItemAndFluidToRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class RTExchangingRecipe(
    itemIngredient: HTItemIngredient,
    fluidIngredient: HTFluidIngredient,
    result: HTItemOrFluidResult,
    progressData: HTProgressData
) : HTItemAndFluidToRecipe.BasicItemAndFluid(itemIngredient, fluidIngredient, result, progressData),
    HTSerializableRecipe<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val SERIALIZER: RecipeSerializer<RTExchangingRecipe> =
            RecipeSerializer(codec(::RTExchangingRecipe), streamCodec(::RTExchangingRecipe))
    }

    override fun getSerializer(): RecipeSerializer<RTExchangingRecipe> = RagiumRecipeSerializers.EXCHANGING

    override fun getType(): RecipeType<RTExchangingRecipe> = RagiumRecipeTypes.EXCHANGING
}
