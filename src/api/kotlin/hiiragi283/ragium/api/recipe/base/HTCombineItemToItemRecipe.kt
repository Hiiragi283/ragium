package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTDoubleItemToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack

abstract class HTCombineItemToItemRecipe(val ingredients: List<HTItemIngredient>, val result: HTItemResult) : HTDoubleItemToItemRecipe {
    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.get()

    final override fun testFirstItem(stack: ItemStack): Boolean = ingredients[0].test(stack)

    final override fun testSecondItem(stack: ItemStack): Boolean = ingredients[1].test(stack)

    final override fun isIncomplete(): Boolean =
        ingredients.isEmpty() || ingredients.all(HTItemIngredient::hasNoMatchingStacks) || result.hasNoMatchingStack
}
