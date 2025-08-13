package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

abstract class HTItemToChancedItemRecipeBase(val ingredient: HTItemIngredient, val results: List<HTItemResult>, val chances: List<Float>) :
    HTItemToChancedItemRecipe {
    final override fun getIngredientCount(input: SingleRecipeInput): Int = ingredient.getRequiredAmount(input.item())

    final override fun getResultItems(input: SingleRecipeInput): List<Pair<ItemStack, Float>> = results
        .mapIndexed { index: Int, result: HTItemResult ->
            val chance: Float = chances.getOrNull(index) ?: 1f
            result.getOrEmpty() to chance
        }.filterNot { (stack: ItemStack, _) -> stack.isEmpty }

    final override fun test(input: SingleRecipeInput): Boolean = !isIncomplete && ingredient.test(input.item())

    final override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack =
        if (test(input)) getResultItem(registries) else ItemStack.EMPTY

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = results.getOrNull(0)?.getOrEmpty() ?: ItemStack.EMPTY

    final override fun isIncomplete(): Boolean =
        ingredient.hasNoMatchingStacks() || results.isEmpty() || results.all(HTItemResult::hasNoMatchingStack)
}
