package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.input.HTDoubleRecipeInput
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

interface HTDoubleItemToItemRecipe : HTRecipe<HTDoubleRecipeInput> {
    override fun test(input: HTDoubleRecipeInput): Boolean {
        val bool1: Boolean = testFirstItem(input.first) && testSecondItem(input.second)
        val bool2: Boolean = testFirstItem(input.second) && testSecondItem(input.first)
        return !isIncomplete && (bool1 || bool2)
    }

    fun testFirstItem(stack: ItemStack): Boolean

    fun testSecondItem(stack: ItemStack): Boolean

    override fun matches(input: HTDoubleRecipeInput, level: Level): Boolean = test(input)

    override fun assemble(input: HTDoubleRecipeInput, registries: HolderLookup.Provider): ItemStack =
        if (test(input)) getResultItem(registries) else ItemStack.EMPTY
}
