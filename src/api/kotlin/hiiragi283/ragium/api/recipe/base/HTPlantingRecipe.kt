package hiiragi283.ragium.api.recipe.base

import hiiragi283.core.api.recipe.base.HTRecipeFactories
import hiiragi283.core.api.recipe.base.HTRecipePredicates
import hiiragi283.core.api.recipe.progress.HTBiProgressProvider
import net.minecraft.world.item.ItemStack

interface HTPlantingRecipe :
    HTRecipePredicates.DoubleItem,
    HTRecipeFactories.DoubleItem<Iterable<ItemStack>>,
    HTBiProgressProvider<ItemStack, ItemStack> {
    fun getRequiredPlantStack(first: ItemStack): ItemStack

    override fun getMatchingStacks(first: ItemStack, second: ItemStack): Pair<ItemStack, ItemStack> = getRequiredPlantStack(first) to ItemStack.EMPTY
}
