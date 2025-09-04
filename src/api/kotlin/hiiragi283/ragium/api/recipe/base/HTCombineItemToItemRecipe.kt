package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTMultiItemToObjRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiItemRecipeInput
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack

abstract class HTCombineItemToItemRecipe(val ingredients: List<HTItemIngredient>, val result: HTItemResult) : HTMultiItemToObjRecipe {
    final override fun test(input: HTMultiItemRecipeInput): Boolean = HTMultiItemToObjRecipe.hasMatchingSlots(ingredients, input.items)

    final override fun assemble(input: HTMultiItemRecipeInput, registries: HolderLookup.Provider): ItemStack =
        getItemResult(input, registries, result)

    final override fun isIncomplete(): Boolean =
        ingredients.isEmpty() || ingredients.any(HTItemIngredient::hasNoMatchingStacks) || result.hasNoMatchingStack()
}
