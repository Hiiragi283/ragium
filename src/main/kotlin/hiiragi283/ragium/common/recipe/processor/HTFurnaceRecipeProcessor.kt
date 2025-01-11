package hiiragi283.ragium.common.recipe.processor

import hiiragi283.ragium.api.extension.mergeStack
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeProcessor
import hiiragi283.ragium.api.util.HTMachineException
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.AbstractCookingRecipe
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.SingleStackRecipeInput
import net.minecraft.world.World
import kotlin.math.min

class HTFurnaceRecipeProcessor<T : AbstractCookingRecipe>(
    recipeType: RecipeType<T>,
    private val inventory: Inventory,
    private val inputIndex: Int,
    private val outputIndex: Int,
) : HTRecipeProcessor {
    private val recipeCache: HTRecipeCache<SingleStackRecipeInput, T> = HTRecipeCache(recipeType)

    override fun process(world: World, tier: HTMachineTier): Result<Unit> {
        val inputStack: ItemStack = inventory.getStack(inputIndex)
        val processCount: Int = min(inputStack.count, tier.smelterMulti)
        return recipeCache
            .getFirstMatch(SingleStackRecipeInput(inputStack), world)
            .runCatching {
                val recipe: T = getOrThrow(HTMachineException::NoMatchingRecipe)
                val resultStack: ItemStack = recipe.getResult(world.registryManager).copy()
                resultStack.count *= processCount
                val output: HTItemResult = HTItemResult.fromStack(resultStack)
                if (!output.canMerge(inventory.getStack(outputIndex))) {
                    throw HTMachineException.MergeResult(false)
                }
                inventory.mergeStack(outputIndex, output)
                inputStack.decrement(processCount)
            }
    }
}
