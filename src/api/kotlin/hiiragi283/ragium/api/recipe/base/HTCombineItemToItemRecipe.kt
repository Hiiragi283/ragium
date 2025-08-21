package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTMultiItemToObjRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiItemRecipeInput
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack

abstract class HTCombineItemToItemRecipe(val ingredients: List<HTItemIngredient>, val result: HTItemResult) : HTMultiItemToObjRecipe {
    final override fun test(input: HTMultiItemRecipeInput): Boolean = HTMultiItemToObjRecipe.hasMatchingSlots(ingredients, input.items)

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getOrEmpty(registries)

    final override fun isIncomplete(): Boolean =
        ingredients.isEmpty() || ingredients.all(HTItemIngredient::hasNoMatchingStacks) || result.hasNoMatchingStack()
}
