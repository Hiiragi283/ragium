package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput

abstract class HTItemToChancedItemRecipeBase(val ingredient: HTItemIngredient, val results: List<HTItemToChancedItemRecipe.ChancedResult>) :
    HTItemToChancedItemRecipe {
    final override fun getIngredientCount(input: SingleRecipeInput): Int = ingredient.getRequiredAmount(input.item())

    final override fun getResultItems(input: SingleRecipeInput): List<HTItemToChancedItemRecipe.ChancedResult> = results

    final override fun test(input: SingleRecipeInput): Boolean = !isIncomplete && ingredient.test(input.item())

    final override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack =
        getItemResult(input, registries, results.getOrNull(0))

    final override fun isIncomplete(): Boolean =
        ingredient.hasNoMatchingStacks() || results.isEmpty() || results.all(HTItemToChancedItemRecipe.ChancedResult::hasNoMatchingStack)
}
