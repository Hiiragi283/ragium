package hiiragi283.ragium.api.recipe.processor

import hiiragi283.ragium.api.extension.modifyStack
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTItemResult
import hiiragi283.ragium.api.recipe.HTRecipeCache
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.AbstractCookingRecipe
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
) : HTRecipeProcessor {
    private val matchGetter: HTRecipeCache<SingleStackRecipeInput, T> = HTRecipeCache(recipeType)

    override fun process(world: World, key: HTMachineKey, tier: HTMachineTier): Boolean = runCatching {
        val inputStack: ItemStack = inventory.getStack(inputIndex)
        val processCount: Int = min(inputStack.count, tier.smelterMulti)
        val recipe: T = matchGetter.getFirstMatch(SingleStackRecipeInput(inputStack), world).getOrNull()?.value
            ?: return@runCatching false
        val resultStack: ItemStack = recipe.getResult(world.registryManager).copy()
        resultStack.count *= processCount
        val output = HTItemResult(resultStack)
        if (!output.canMerge(inventory.getStack(outputIndex))) return@runCatching false
        inventory.modifyStack(outputIndex, output::merge)
        inputStack.decrement(processCount)
        return@runCatching true
    }.getOrDefault(false)
}
