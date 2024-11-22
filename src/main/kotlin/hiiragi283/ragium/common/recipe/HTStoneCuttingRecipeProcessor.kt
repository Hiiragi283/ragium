package hiiragi283.ragium.common.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.modifyStack
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeProcessor
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

    override fun process(world: World, key: HTMachineKey, tier: HTMachineTier): DataResult<Unit> {
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
                    }?.let(DataResult<StonecuttingRecipe>::success)
                    ?: DataResult.error { "Failed to find matching recipe" }
            }.flatMap { recipe: StonecuttingRecipe ->
                val resultStack: ItemStack = recipe.getResult(world.registryManager).copy()
                val output = HTItemResult(resultStack)
                if (!output.canMerge(
                        inventory.getStack(outputIndex),
                    )
                ) {
                    return@flatMap DataResult.error { "Failed to merge result into outputs!" }
                }
                inventory.modifyStack(outputIndex, output::merge)
                inputStack.count -= 1
                DataResult.success(Unit)
            }
    }
}
