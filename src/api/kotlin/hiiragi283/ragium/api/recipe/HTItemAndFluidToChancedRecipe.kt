package hiiragi283.ragium.api.recipe

import hiiragi283.core.api.recipe.HTChancedRecipe
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput

interface HTItemAndFluidToChancedRecipe : HTChancedRecipe<HTItemAndFluidRecipeInput> {
    fun getRequiredFluidAmount(input: HTItemAndFluidRecipeInput): Int

    fun getRequiredItemAmount(input: HTItemAndFluidRecipeInput): Int

    //    Serializable    //

    interface Serializable :
        HTItemAndFluidToChancedRecipe,
        HTChancedRecipe.Serializable<HTItemAndFluidRecipeInput>
}
