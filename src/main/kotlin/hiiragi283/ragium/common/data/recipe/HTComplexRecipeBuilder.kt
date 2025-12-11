package hiiragi283.ragium.common.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.common.data.recipe.base.HTComplexResultRecipeBuilder
import hiiragi283.ragium.common.recipe.HTMixingRecipe
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import hiiragi283.ragium.common.recipe.HTSimpleMixingRecipe

class HTComplexRecipeBuilder(prefix: String, private val factory: Factory<*>) :
    HTComplexResultRecipeBuilder<HTComplexRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun mixing(): HTComplexRecipeBuilder = HTComplexRecipeBuilder(
            RagiumConst.MIXING,
        ) { itemIngredients: List<HTItemIngredient>, fluidIngredients: List<HTFluidIngredient>, results: HTComplexResult ->
            if (itemIngredients.size == 1 && fluidIngredients.size == 1) {
                HTSimpleMixingRecipe(itemIngredients[0], fluidIngredients[0], results)
            } else {
                HTMixingRecipe(itemIngredients, fluidIngredients, results)
            }
        }

        @JvmStatic
        fun refining(): HTComplexRecipeBuilder =
            HTComplexRecipeBuilder(RagiumConst.REFINING) { _, fluidIngredients: List<HTFluidIngredient>, results: HTComplexResult ->
                HTRefiningRecipe(fluidIngredients[0], results)
            }
    }

    private val itemIngredients: MutableList<HTItemIngredient> = mutableListOf()
    private val fluidIngredients: MutableList<HTFluidIngredient> = mutableListOf()

    fun addIngredient(ingredient: HTItemIngredient?): HTComplexRecipeBuilder = apply {
        ingredient?.let(itemIngredients::add)
    }

    fun addIngredient(ingredient: HTFluidIngredient): HTComplexRecipeBuilder = apply {
        fluidIngredients.add(ingredient)
    }

    override fun createRecipe(): HTComplexRecipe = factory.create(
        itemIngredients,
        fluidIngredients,
        toIorResult(),
    )

    fun interface Factory<RECIPE : HTComplexRecipe> {
        fun create(itemIngredients: List<HTItemIngredient>, fluidIngredients: List<HTFluidIngredient>, results: HTComplexResult): RECIPE
    }
}
