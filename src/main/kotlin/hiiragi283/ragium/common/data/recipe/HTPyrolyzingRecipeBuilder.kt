package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import net.minecraft.resources.ResourceLocation

class HTPyrolyzingRecipeBuilder(
    private val ingredient: HTItemIngredient,
    private val itemResult: HTItemResult,
    private val fluidResult: HTFluidResult,
) : HTProcessingRecipeBuilder<HTPyrolyzingRecipeBuilder>(RagiumConst.PYROLYZING) {
    companion object {
        @JvmStatic
        fun create(ingredient: HTItemIngredient, itemResult: HTItemResult, fluidResult: HTFluidResult): HTPyrolyzingRecipeBuilder =
            HTPyrolyzingRecipeBuilder(ingredient, itemResult, fluidResult)
    }

    override fun getPrimalId(): ResourceLocation = fluidResult.getId()

    override fun createRecipe(): HTPyrolyzingRecipe = HTPyrolyzingRecipe(ingredient, itemResult, fluidResult, time, exp)
}
