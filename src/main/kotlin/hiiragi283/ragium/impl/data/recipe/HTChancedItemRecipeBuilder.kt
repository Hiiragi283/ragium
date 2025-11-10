package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.result.HTChancedItemResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.resources.ResourceLocation

abstract class HTChancedItemRecipeBuilder<BUILDER : HTChancedItemRecipeBuilder<BUILDER>>(prefix: String) :
    HTRecipeBuilder.Prefixed(prefix) {
    protected val results: MutableList<HTChancedItemResult> = mutableListOf()

    @Suppress("UNCHECKED_CAST")
    private fun self(): BUILDER = this as BUILDER

    fun addResult(result: HTItemResult, chance: Float = 1f): BUILDER = addResult(HTChancedItemResult(result, chance))

    fun addResult(result: HTChancedItemResult): BUILDER {
        check(result.chance in (0f..1f)) { "Chance of result must be within 0f to 1f!" }
        this.results.add(result)
        return self()
    }

    fun addResults(results: Iterable<HTChancedItemResult>): BUILDER {
        results.forEach(::addResult)
        return self()
    }

    final override fun getPrimalId(): ResourceLocation = results[0].id
}
