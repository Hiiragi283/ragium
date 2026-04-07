package hiiragi283.ragium.api.recipe.base

import hiiragi283.core.api.recipe.base.HTMultiOutputRecipe
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput

interface HTItemFluidMultiOutputRecipe : HTMultiOutputRecipe<HTItemAndFluidRecipeInput> {
    fun getRequiredCount(input: HTItemAndFluidRecipeInput): Int

    fun getRequiredAmount(input: HTItemAndFluidRecipeInput): Int

    //    Serializable    //

    interface Serializable :
        HTItemFluidMultiOutputRecipe,
        HTMultiOutputRecipe.Serializable<HTItemAndFluidRecipeInput>
}
