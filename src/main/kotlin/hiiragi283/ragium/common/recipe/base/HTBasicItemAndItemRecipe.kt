package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.HTItemAndItemRecipe
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack

abstract class HTBasicItemAndItemRecipe(
    val first: HTItemIngredient,
    val second: HTItemIngredient,
    val result: HTItemResult,
    final override val time: Int,
) : HTItemAndItemRecipe {
    override fun testFirstItem(stack: ItemStack): Boolean = first.test(stack)

    override fun testSecondItem(stack: ItemStack): Boolean = second.test(stack)

    override fun assemble(input: HTDoubleRecipeInput, registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)

    override fun getRequiredAmount(input: HTDoubleRecipeInput): Pair<Int, Int> = first.amount to second.amount
}
