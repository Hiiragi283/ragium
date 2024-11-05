package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.extension.modifyStack
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.AbstractCookingRecipe
import net.minecraft.recipe.RecipeEntry
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.SingleStackRecipeInput
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull
import kotlin.math.min

class HTFurnaceRecipeProcessor<T : AbstractCookingRecipe>(
    recipeType: RecipeType<T>,
    private val inventory: Inventory,
    private val inputIndex: Int,
    private val outputIndex: Int,
    private val multiplier: Int = 1,
) {
    private val matchGetter: HTRecipeCache<SingleStackRecipeInput, T> = HTRecipeCache(recipeType)

    fun process(world: World): Boolean {
        val inputStack: ItemStack = inventory.getStack(inputIndex)
        val processCount: Int = min(inputStack.count, multiplier)
        val recipeEntry: RecipeEntry<T> = matchGetter
            .getFirstMatch(SingleStackRecipeInput(inventory.getStack(inputIndex)), world)
            .getOrNull() ?: return false
        val recipe: T = recipeEntry.value
        val resultStack: ItemStack = recipe.getResult(world.registryManager).copy()
        resultStack.count *= processCount
        val output: HTRecipeResult.Item = HTRecipeResult.ofItem(resultStack)
        if (!output.canMerge(inventory.getStack(outputIndex))) return false
        inventory.modifyStack(outputIndex, output::merge)
        inventory.getStack(inputIndex).count -= processCount
        return true
    }
}
