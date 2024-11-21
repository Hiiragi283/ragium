package hiiragi283.ragium.api.recipe.processor

import hiiragi283.ragium.api.extension.modifyStack
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTItemResult
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeEntry
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
    override fun process(world: World, key: HTMachineKey, tier: HTMachineTier): Boolean = runCatching {
        val inputStack: ItemStack = inventory.getStack(inputIndex)
        val targetStack: ItemStack = inventory.getStack(targetIndex)
        val recipeEntry: RecipeEntry<StonecuttingRecipe> =
            world.recipeManager
                .getAllMatches(RecipeType.STONECUTTING, SingleStackRecipeInput(inputStack), world)
                .firstOrNull { entry: RecipeEntry<StonecuttingRecipe> ->
                    ItemStack.areItemsAndComponentsEqual(
                        entry.value.getResult(world.registryManager),
                        targetStack,
                    )
                }
                ?: return@runCatching false
        val recipe: StonecuttingRecipe = recipeEntry.value
        val resultStack: ItemStack = recipe.getResult(world.registryManager).copy()
        val output = HTItemResult(resultStack)
        if (!output.canMerge(inventory.getStack(outputIndex))) return@runCatching false
        inventory.modifyStack(outputIndex, output::merge)
        inputStack.count -= 1
        return@runCatching true
    }.getOrDefault(false)
}
