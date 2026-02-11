package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import net.minecraft.world.level.Level

abstract class HTFluidWithItemRecipe(
    val fluidIngredient: HTFluidIngredient,
    val itemIngredient: HTItemIngredient,
    parameters: SubParameters,
) : HTProcessingRecipe<HTItemAndFluidRecipeInput>(parameters) {
    final override fun matches(input: HTItemAndFluidRecipeInput, level: Level): Boolean =
        itemIngredient.test(input.item) && fluidIngredient.test(input.fluid)
}
