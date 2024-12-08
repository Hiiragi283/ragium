package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.extension.getStackOrEmpty
import hiiragi283.ragium.api.extension.iterable
import hiiragi283.ragium.api.extension.modifyStack
import hiiragi283.ragium.api.extension.unitMap
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeProcessor
import hiiragi283.ragium.api.util.HTUnitResult
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.SmithingRecipe
import net.minecraft.recipe.input.SmithingRecipeInput
import net.minecraft.world.World

class HTSmithingRecipeProcessor(private val inventory: Inventory, private val inputIndex: IntArray, private val outputIndex: Int) :
    HTRecipeProcessor {
    private val recipeCache: HTRecipeCache<SmithingRecipeInput, SmithingRecipe> = HTRecipeCache(RecipeType.SMITHING)

    override fun process(world: World, key: HTMachineKey, tier: HTMachineTier): HTUnitResult {
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
            .unitMap { recipe: SmithingRecipe ->
                val resultStack: ItemStack = recipe.craft(input, world.registryManager).copy()
                val output = HTItemResult(resultStack)
                if (!output.canMerge(
                        inventory.getStack(outputIndex),
                    )
                ) {
                    return@unitMap HTUnitResult.errorString { "Failed to merge result into output!" }
                }
                inventory.modifyStack(outputIndex, output::merge)
                input.iterable().forEach { stackIn: ItemStack -> stackIn.decrement(1) }
                HTUnitResult.success()
            }
    }
}
