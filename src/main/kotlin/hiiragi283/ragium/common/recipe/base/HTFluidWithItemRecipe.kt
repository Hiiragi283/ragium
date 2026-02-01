package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.HTViewProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack

abstract class HTFluidWithItemRecipe(
    val fluidIngredient: HTFluidIngredient,
    val itemIngredient: HTItemIngredient,
    val result: HTItemResult,
    parameters: SubParameters,
) : HTViewProcessingRecipe(parameters) {
    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)
}
