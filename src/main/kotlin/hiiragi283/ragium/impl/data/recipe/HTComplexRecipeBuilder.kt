package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.api.util.toIor
import hiiragi283.ragium.impl.recipe.HTMixingRecipe
import hiiragi283.ragium.impl.recipe.HTRefiningRecipe
import net.minecraft.resources.ResourceLocation

class HTComplexRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTRecipeBuilder<HTComplexRecipeBuilder>(prefix) {
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
            builder.setResult(fluidResult)
            itemResult?.let(builder::setResult)
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
    private var itemResult: HTItemResult? = null
    private var fluidResult: HTFluidResult? = null

    fun addIngredient(ingredient: HTItemIngredient): HTComplexRecipeBuilder = apply {
        check(itemIngredients.size <= 2) { "Item ingredients already initialized!" }
        itemIngredients.add(ingredient)
    }

    fun addIngredient(ingredient: HTFluidIngredient): HTComplexRecipeBuilder = apply {
        check(fluidIngredients.size <= 2) { "Fluid ingredients already initialized!" }
        fluidIngredients.add(ingredient)
    }

    fun setResult(result: HTItemResult): HTComplexRecipeBuilder = apply {
        check(this.itemResult == null) { "Item result already initialized!" }
        this.itemResult = result
    }

    fun setResult(result: HTFluidResult): HTComplexRecipeBuilder = apply {
        check(this.fluidResult == null) { "Fluid result already initialized!" }
        this.fluidResult = result
    }

    override fun createRecipe(): HTComplexRecipe = factory.create(
        itemIngredients,
        fluidIngredients,
        toIorResult(),
    )

    private fun toIorResult(): Ior<HTItemResult, HTFluidResult> =
        (itemResult to fluidResult).toIor() ?: error("Either item or fluid result required")

    override fun getPrimalId(): ResourceLocation = toIorResult().map(HTItemResult::id, HTFluidResult::id)

    fun interface Factory<RECIPE : HTComplexRecipe> {
        fun create(
            itemIngredients: List<HTItemIngredient>,
            fluidIngredients: List<HTFluidIngredient>,
            results: Ior<HTItemResult, HTFluidResult>,
        ): RECIPE
    }
}
