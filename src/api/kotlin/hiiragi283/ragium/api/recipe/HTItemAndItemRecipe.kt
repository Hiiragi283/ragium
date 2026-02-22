package hiiragi283.ragium.api.recipe

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

interface HTItemAndItemRecipe : HTProcessingRecipe<HTDoubleRecipeInput> {
    override fun matches(input: HTDoubleRecipeInput, level: Level): Boolean {
        val (first: ItemStack, second: ItemStack) = input
        return testFirstItem(first) && testSecondItem(second)
    }

    fun testFirstItem(stack: ItemStack): Boolean

    fun testSecondItem(stack: ItemStack): Boolean

    override fun assemble(input: HTDoubleRecipeInput, registries: HolderLookup.Provider): ItemStack

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    fun getRequiredAmount(input: HTDoubleRecipeInput): Pair<Int, Int>
}
