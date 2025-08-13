package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTDoubleItemToItemRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import java.util.*

abstract class HTItemWithCatalystToItemRecipe(
    val ingredient: HTItemIngredient,
    val catalyst: Optional<HTItemIngredient>,
    val result: HTItemResult,
) : HTDoubleItemToItemRecipe {
    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.getOrEmpty()

    final override fun testFirstItem(stack: ItemStack): Boolean = ingredient.test(stack)

    final override fun testSecondItem(stack: ItemStack): Boolean =
        catalyst.map { ingredient1: HTItemIngredient -> ingredient1.testOnlyType(stack) }.orElse(stack.isEmpty)

    final override fun isIncomplete(): Boolean {
        val bool1: Boolean = ingredient.hasNoMatchingStacks()
        val bool2: Boolean = catalyst.map(HTItemIngredient::hasNoMatchingStacks).orElse(false)
        val bool3: Boolean = result.hasNoMatchingStack
        return bool1 || bool2 || bool3
    }
}
