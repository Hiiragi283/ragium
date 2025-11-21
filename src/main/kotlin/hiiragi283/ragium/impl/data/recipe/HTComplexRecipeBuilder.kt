package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.impl.recipe.HTMixingRecipe
import hiiragi283.ragium.impl.recipe.HTRefiningRecipe

class HTComplexRecipeBuilder(prefix: String, private val factory: Factory<*>) :
    HTComplexResultRecipeBuilder<HTComplexRecipeBuilder>(prefix) {
    companion object {
        @JvmStatic
        fun mixing(): HTComplexRecipeBuilder = HTComplexRecipeBuilder(RagiumConst.MIXING, ::HTMixingRecipe)

        @JvmStatic
        fun refining(prefix: String): HTComplexRecipeBuilder = HTComplexRecipeBuilder(prefix, ::HTRefiningRecipe)

        @JvmStatic
        fun refining(
            fluidIngredient: HTFluidIngredient,
            fluidResult: HTFluidResult,
            itemIngredient: HTItemIngredient?,
            itemResult: HTItemResult?,
        ): HTComplexRecipeBuilder {
            val builder: HTComplexRecipeBuilder = refining(RagiumConst.REFINING)
            // inputs
            builder.addIngredient(fluidIngredient)
            itemIngredient?.let(builder::addIngredient)
            // outputs
            builder.setResult(itemResult)
            builder.setResult(fluidResult)
            return builder
        }

        @JvmStatic
        fun solidifying(
            itemIngredient: HTItemIngredient?,
            fluidIngredient: HTFluidIngredient,
            itemResult: HTItemResult,
        ): HTComplexRecipeBuilder {
            val builder: HTComplexRecipeBuilder = refining(RagiumConst.SOLIDIFYING)
            // inputs
            builder.addIngredient(fluidIngredient)
            itemIngredient?.let(builder::addIngredient)
            // outputs
            builder.setResult(itemResult)
            return builder
        }
    }

    private val itemIngredients: MutableList<HTItemIngredient> = mutableListOf()
    private val fluidIngredients: MutableList<HTFluidIngredient> = mutableListOf()

    fun addIngredient(ingredient: HTItemIngredient): HTComplexRecipeBuilder = apply {
        check(itemIngredients.size <= 2) { "Item ingredients already initialized!" }
        itemIngredients.add(ingredient)
    }

    fun addIngredient(ingredient: HTFluidIngredient): HTComplexRecipeBuilder = apply {
        check(fluidIngredients.size <= 2) { "Fluid ingredients already initialized!" }
        fluidIngredients.add(ingredient)
    }

    override fun createRecipe(): HTComplexRecipe = factory.create(
        itemIngredients,
        fluidIngredients,
        toIorResult(),
    )

    fun interface Factory<RECIPE : HTComplexRecipe> {
        fun create(
            itemIngredients: List<HTItemIngredient>,
            fluidIngredients: List<HTFluidIngredient>,
            results: Ior<HTItemResult, HTFluidResult>,
        ): RECIPE
    }
}
