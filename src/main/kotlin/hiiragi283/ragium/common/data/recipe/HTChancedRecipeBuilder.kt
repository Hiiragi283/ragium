package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.toFraction
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

abstract class HTChancedRecipeBuilder(prefix: String) : HTProcessingRecipeBuilder(prefix) {
    lateinit var result: HTItemResult
    val chancedResults = ChancedResults()

    final override fun getPrimalId(): ResourceLocation = result.getId()

    //    ChancedResults    //

    inner class ChancedResults {
        val results: List<HTChancedItemResult> get() = _results
        private val _results: MutableList<HTChancedItemResult> = mutableListOf()

        @JvmName("addResult")
        operator fun plusAssign(result: HTItemResult) {
            this.plusAssign(HTChancedItemResult(result, Fraction.ONE, null)) // TODO
        }

        @JvmName("addResult")
        operator fun plusAssign(pair: Pair<HTItemResult, Float>) {
            val (result: HTItemResult, chance: Float) = pair
            this.plusAssign(HTChancedItemResult(result, chance.toFraction(), null)) // TODO
        }

        @JvmName("addResultWithFraction")
        operator fun plusAssign(pair: Pair<HTItemResult, Fraction>) {
            val (result: HTItemResult, chance: Fraction) = pair
            this.plusAssign(HTChancedItemResult(result, chance, null)) // TODO
        }

        @JvmName("addResult")
        operator fun plusAssign(result: HTChancedItemResult) {
            _results += result
        }
    }
}
