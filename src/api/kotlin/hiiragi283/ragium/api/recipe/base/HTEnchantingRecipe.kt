package hiiragi283.ragium.api.recipe.base

import hiiragi283.core.api.recipe.HTRecipeFactory
import hiiragi283.core.api.recipe.base.HTRecipePredicates
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

interface HTEnchantingRecipe :
    HTRecipePredicates.TripleInput<HTEnchantingRecipe.Input, ItemStack, ItemStack, Int>,
    HTRecipeFactory<HTEnchantingRecipe.Input, ItemStack> {
    fun getRequiredExpAmount(base: ItemStack, addition: ItemStack): Int

    fun getRequiredAdditionAmount(base: ItemStack, addition: ItemStack, expAmount: Int): Int

    override fun matches(input: Input): Boolean {
        val (base: ItemStack, addition: ItemStack, expAmount: Int) = input
        return test(base, addition, expAmount)
    }

    @JvmRecord
    data class Input(val base: ItemStack, val addition: ItemStack, val expAmount: Int) : RecipeInput {
        override fun getItem(index: Int): ItemStack = when (index) {
            0 -> base
            1 -> addition
            else -> error("No item for index: $index")
        }

        override fun size(): Int = 2
    }
}
