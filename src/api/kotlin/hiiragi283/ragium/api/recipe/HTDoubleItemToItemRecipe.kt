package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

abstract class HTDoubleItemToItemRecipe(private val recipeType: RecipeType<*>, val result: HTItemResult) :
    HTRecipe<HTDoubleRecipeInput> {
    final override fun test(input: HTDoubleRecipeInput): Boolean {
        val bool1: Boolean = testFirstItem(input.first) && testSecondItem(input.second)
        val bool2: Boolean = testFirstItem(input.second) && testSecondItem(input.first)
        return !isIncomplete && (bool1 || bool2)
    }

    abstract fun testFirstItem(stack: ItemStack): Boolean

    abstract fun testSecondItem(stack: ItemStack): Boolean

    override fun matches(input: HTDoubleRecipeInput, level: Level): Boolean = test(input)

    final override fun assemble(input: HTDoubleRecipeInput, registries: HolderLookup.Provider): ItemStack =
        if (test(input)) getResultItem(registries) else ItemStack.EMPTY

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.get()

    final override fun getType(): RecipeType<*> = recipeType
}
