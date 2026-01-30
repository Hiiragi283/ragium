package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import org.apache.commons.lang3.math.Fraction

abstract class HTFluidWithItemRecipe(
    val fluidIngredient: HTFluidIngredient,
    val itemIngredient: HTItemIngredient,
    val result: HTItemResult,
    time: Int,
    exp: Fraction,
) : HTProcessingRecipe<HTItemAndFluidRecipeInput>(time, exp) {
    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)
}
