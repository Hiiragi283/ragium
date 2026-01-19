package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import net.minecraft.resources.ResourceLocation

class HTMixingRecipeBuilder : HTAbstractComplexRecipeBuilder<HTMixingRecipeBuilder>(RagiumConst.MIXING) {
    companion object {
        @JvmStatic
        fun create(): HTMixingRecipeBuilder = HTMixingRecipeBuilder()
    }

    private val itemIngredients: MutableList<HTItemIngredient> = mutableListOf()
    private val fluidIngredients: MutableList<HTFluidIngredient> = mutableListOf()

    fun addIngredient(ingredient: HTItemIngredient): HTMixingRecipeBuilder = apply {
        itemIngredients += ingredient
    }

    fun addIngredient(ingredient: HTFluidIngredient): HTMixingRecipeBuilder = apply {
        fluidIngredients += ingredient
    }

    override fun getPrimalId(): ResourceLocation = toIorResult().map(HTItemResult::getId, HTFluidResult::getId)

    override fun createRecipe(): HTMixingRecipe = HTMixingRecipe(
        itemIngredients,
        fluidIngredients,
        toIorResult(),
        time,
        exp,
    )
}
