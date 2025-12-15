package hiiragi283.ragium.common.recipe.base

import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTFluidWithCatalystRecipe
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.level.Level
import java.util.*

/**
 * [HTFluidWithCatalystRecipe]の抽象クラス
 */
abstract class HTBasicFluidWithCatalystRecipe(
    val ingredient: HTFluidIngredient,
    val catalyst: Optional<HTItemIngredient>,
    result: HTItemResult,
) : HTBasicSingleOutputRecipe(result),
    HTFluidWithCatalystRecipe {
    final override fun matches(input: HTRecipeInput, level: Level): Boolean {
        val bool1: Boolean = input.testFluid(0, ingredient)
        val bool2: Boolean = input.testCatalyst(0, catalyst)
        return bool1 && bool2
    }

    override fun getRequiredAmount(): Int = ingredient.getRequiredAmount()
}
