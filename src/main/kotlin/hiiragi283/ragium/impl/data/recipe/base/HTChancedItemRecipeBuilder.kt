package hiiragi283.ragium.impl.data.recipe.base

import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.chance.HTItemResultWithChance
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

abstract class HTChancedItemRecipeBuilder<BUILDER : HTChancedItemRecipeBuilder<BUILDER>>(prefix: String) :
    HTRecipeBuilder<BUILDER>(prefix) {
    protected val results: MutableList<HTItemResultWithChance> = mutableListOf()

    fun addResult(result: HTItemResult, chance: Fraction = Fraction.ONE): BUILDER = addResult(HTItemResultWithChance(result, chance))

    fun addResult(result: HTItemResultWithChance): BUILDER {
        check(result.chance in (Fraction.ZERO..Fraction.ONE)) { "Chance of result must be within 0f to 1f!" }
        this.results.add(result)
        return self()
    }

    final override fun getPrimalId(): ResourceLocation = results[0].id
}
