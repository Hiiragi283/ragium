package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.common.recipe.input.HTSingleCatalystRecipeInput
import net.minecraft.world.level.Level

abstract class HTSingleCatalystRecipe<ING : HTIngredient<*, *>>(
    val ingredient: ING,
    val catalyst: HTItemIngredient,
    parameters: SubParameters,
) : HTProcessingRecipe<HTSingleCatalystRecipeInput>(parameters) {
    override fun matches(input: HTSingleCatalystRecipeInput, level: Level): Boolean {
        val bool1: Boolean = matchIngredient(input)
        val bool2: Boolean = this.catalyst.testOnlyType(input.catalyst)
        return bool1 && bool2
    }

    protected abstract fun matchIngredient(input: HTSingleCatalystRecipeInput): Boolean
}
