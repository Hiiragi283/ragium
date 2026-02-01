package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.toFraction
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTCrushingRecipe
import hiiragi283.ragium.common.recipe.HTCuttingRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import hiiragi283.ragium.common.recipe.base.HTChancedRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

class HTChancedRecipeBuilder<ING : Any>(prefix: String, private val factory: Factory<ING, *>) : HTProcessingRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun crushing(output: RecipeOutput, builderAction: HTChancedRecipeBuilder<HTItemIngredient>.() -> Unit) {
            HTChancedRecipeBuilder(RagiumConst.CRUSHING, ::HTCrushingRecipe).apply(builderAction).save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun cutting(output: RecipeOutput, builderAction: HTChancedRecipeBuilder<HTItemIngredient>.() -> Unit) {
            HTChancedRecipeBuilder(RagiumConst.CUTTING, ::HTCuttingRecipe).apply(builderAction).save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun washing(
            output: RecipeOutput,
            builderAction: HTChancedRecipeBuilder<Pair<HTItemIngredient, HTFluidIngredient>>.() -> Unit,
        ) {
            HTChancedRecipeBuilder(RagiumConst.WASHING, ::HTWashingRecipe).apply(builderAction).save(output)
        }
    }

    lateinit var ingredient: ING
    lateinit var result: HTItemResult
    val chancedResults = ChancedResults()

    init {
        time /= 2
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTChancedRecipe = factory.create(ingredient, result, chancedResults.results, subParameters())

    //    ChancedResults    //

    inner class ChancedResults {
        val results: List<HTChancedItemResult> get() = _results
        private val _results: MutableList<HTChancedItemResult> = mutableListOf()

        @JvmName("addResult")
        operator fun plusAssign(result: HTItemResult) {
            this.plusAssign(HTChancedItemResult(result, Fraction.ONE))
        }

        @JvmName("addResult")
        operator fun plusAssign(pair: Pair<HTItemResult, Float>) {
            val (result: HTItemResult, chance: Float) = pair
            this.plusAssign(HTChancedItemResult(result, chance.toFraction()))
        }

        @JvmName("addResultWithFraction")
        operator fun plusAssign(pair: Pair<HTItemResult, Fraction>) {
            val (result: HTItemResult, chance: Fraction) = pair
            this.plusAssign(HTChancedItemResult(result, chance))
        }

        @JvmName("addResult")
        operator fun plusAssign(result: HTChancedItemResult) {
            _results += result
        }
    }

    //    Factory    //

    fun interface Factory<ING : Any, RECIPE : HTChancedRecipe> {
        fun create(
            ingredient: ING,
            result: HTItemResult,
            extraResults: List<HTChancedItemResult>,
            parameters: HTProcessingRecipe.SubParameters,
        ): RECIPE
    }
}
