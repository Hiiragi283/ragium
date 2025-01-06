package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.extension.mergeStack
import hiiragi283.ragium.api.extension.toDataResult
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
import net.minecraft.recipe.StonecuttingRecipe
import net.minecraft.recipe.input.SingleStackRecipeInput
import net.minecraft.world.World

class HTStoneCuttingRecipeProcessor(
    private val inventory: Inventory,
    private val inputIndex: Int,
    private val outputIndex: Int,
    private val targetIndex: Int,
) : HTRecipeProcessor {
    private val recipeCache: HTRecipeCache<SingleStackRecipeInput, StonecuttingRecipe> =
        HTRecipeCache(RecipeType.STONECUTTING)

    override fun process(world: World, key: HTMachineKey, tier: HTMachineTier): HTUnitResult {
        val inputStack: ItemStack = inventory.getStack(inputIndex)
        val targetStack: ItemStack = inventory.getStack(targetIndex)
        return recipeCache
            .getAllMatches(SingleStackRecipeInput(inputStack), world)
            .flatMap { recipes: List<StonecuttingRecipe> ->
                recipes
                    .firstOrNull { recipe: StonecuttingRecipe ->
                        ItemStack.areItemsAndComponentsEqual(
                            recipe.getResult(world.registryManager),
                            targetStack,
                        )
                    }.toDataResult { "Failed to find matching recipe" }
            }.unitMap { recipe: StonecuttingRecipe ->
                val resultStack: ItemStack = recipe.getResult(world.registryManager).copy()
                val output = HTItemResult(resultStack)
                if (!output.canMerge(
                        inventory.getStack(outputIndex),
                    )
                ) {
                    return@unitMap HTUnitResult.errorString { "Failed to merge result into outputs!" }
                }
                inventory.mergeStack(outputIndex, output)
                inputStack.count -= 1
                HTUnitResult.success()
            }
    }
}
