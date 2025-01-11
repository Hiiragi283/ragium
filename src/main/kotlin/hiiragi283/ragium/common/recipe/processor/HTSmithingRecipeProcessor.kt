package hiiragi283.ragium.common.recipe.processor

import hiiragi283.ragium.api.extension.getStackOrEmpty
import hiiragi283.ragium.api.extension.mergeStack
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeProcessor
import hiiragi283.ragium.api.util.HTMachineException
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.SmithingRecipe
import net.minecraft.recipe.input.SmithingRecipeInput
import net.minecraft.world.World

class HTSmithingRecipeProcessor(private val inventory: Inventory, private val inputIndex: IntArray, private val outputIndex: Int) :
    HTRecipeProcessor {
    private val recipeCache: HTRecipeCache<SmithingRecipeInput, SmithingRecipe> = HTRecipeCache(RecipeType.SMITHING)

    override fun process(world: World, key: HTMachineKey, tier: HTMachineTier): Result<Unit> {
        val input: SmithingRecipeInput = inputIndex
            .map(inventory::getStackOrEmpty)
            .let {
                SmithingRecipeInput(
                    it.getOrNull(0) ?: ItemStack.EMPTY,
                    it.getOrNull(1) ?: ItemStack.EMPTY,
                    it.getOrNull(2) ?: ItemStack.EMPTY,
                )
            }
        return recipeCache
            .getFirstMatch(input, world)
            .runCatching {
                val recipe: SmithingRecipe = getOrThrow(HTMachineException::NoMatchingRecipe)
                val resultStack: ItemStack = recipe.craft(input, world.registryManager).copy()
                val output: HTItemResult = HTItemResult.fromStack(resultStack)
                if (!output.canMerge(inventory.getStack(outputIndex))) {
                    throw HTMachineException.MergeResult(false)
                }
                inventory.mergeStack(outputIndex, output)
                input.template.decrement(1)
                input.base.decrement(1)
                input.addition.decrement(1)
            }
    }
}
