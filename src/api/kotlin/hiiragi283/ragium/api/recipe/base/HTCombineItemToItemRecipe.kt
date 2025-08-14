package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTMultiItemToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiItemRecipeInput
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack

abstract class HTCombineItemToItemRecipe(val ingredients: List<HTItemIngredient>, val result: HTItemResult) : HTMultiItemToItemRecipe {
    override fun test(input: HTMultiItemRecipeInput): Boolean = HTMultiItemToItemRecipe.hasMatchingSlots(ingredients, input.items)

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getOrEmpty()

    final override fun isIncomplete(): Boolean =
        ingredients.isEmpty() || ingredients.all(HTItemIngredient::hasNoMatchingStacks) || result.hasNoMatchingStack
}
