package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import net.minecraft.resources.ResourceLocation

class HTRefiningRecipeBuilder(val ingredient: HTFluidIngredient, val result: HTFluidResult) :
    HTAbstractComplexRecipeBuilder<HTRefiningRecipeBuilder>(RagiumConst.REFINING) {
    companion object {
        @JvmStatic
        fun create(ingredient: HTFluidIngredient, result: HTFluidResult): HTRefiningRecipeBuilder =
            HTRefiningRecipeBuilder(ingredient, result)
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTRefiningRecipe = HTRefiningRecipe(
        ingredient,
        result,
        toIorResult(),
        time,
        exp,
    )
}
