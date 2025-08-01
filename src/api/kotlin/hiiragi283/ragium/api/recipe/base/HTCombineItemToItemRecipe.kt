package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTDoubleItemToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType

abstract class HTCombineItemToItemRecipe(recipeType: RecipeType<*>, val ingredients: List<HTItemIngredient>, result: HTItemResult) :
    HTDoubleItemToItemRecipe(recipeType, result) {
    final override fun testFirstItem(stack: ItemStack): Boolean = ingredients[0].test(stack)

    final override fun testSecondItem(stack: ItemStack): Boolean = ingredients[1].test(stack)

    final override fun isIncomplete(): Boolean =
        ingredients.isEmpty() || ingredients.all(HTItemIngredient::hasNoMatchingStacks) || result.hasNoMatchingStack
}
