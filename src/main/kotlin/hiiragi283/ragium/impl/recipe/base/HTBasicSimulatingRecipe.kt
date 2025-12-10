package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTSimulatingRecipe
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import net.minecraft.world.level.Level
import java.util.Optional

abstract class HTBasicSimulatingRecipe<T : Any>(val ingredient: Optional<HTItemIngredient>, val catalyst: T, results: HTComplexResult) :
    HTBasicComplexOutputRecipe(results),
    HTSimulatingRecipe {
    final override fun matches(input: HTRecipeInput, level: Level): Boolean {
        val bool1: Boolean = testCatalyst(input, level)
        val bool2: Boolean = input.testItem(0, ingredient)
        return bool1 && bool2
    }

    protected abstract fun testCatalyst(input: HTRecipeInput, level: Level): Boolean

    final override fun getRequiredCount(): Int = ingredient.map(HTItemIngredient::getRequiredAmount).orElse(0)
}
