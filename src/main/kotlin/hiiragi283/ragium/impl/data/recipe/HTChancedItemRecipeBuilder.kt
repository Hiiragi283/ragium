package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.HTChancedItemRecipe
import hiiragi283.ragium.api.recipe.result.HTChancedItemResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.resources.ResourceLocation

abstract class HTChancedItemRecipeBuilder<RECIPE : HTChancedItemRecipe<*>>(prefix: String) : HTRecipeBuilder.Prefixed(prefix) {
    protected val results: MutableList<HTChancedItemResult> = mutableListOf()

    fun addResult(result: HTItemResult, chance: Float = 1f): HTChancedItemRecipeBuilder<RECIPE> =
        addResult(HTChancedItemResult(result, chance))

    fun addResult(result: HTChancedItemResult): HTChancedItemRecipeBuilder<RECIPE> = apply {
        check(result.chance in (0f..1f)) { "Chance of result must be within 0f to 1f!" }
        this.results.add(result)
    }

    fun addResults(results: Iterable<HTChancedItemResult>): HTChancedItemRecipeBuilder<RECIPE> = apply {
        results.forEach(::addResult)
    }

    final override fun getPrimalId(): ResourceLocation = results[0].id
}
