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

    override fun process(world: World, key: HTMachineKey, tier: HTMachineTier): DataResult<Unit> {
        val inputStack: ItemStack = inventory.getStack(inputIndex)
        val processCount: Int = min(inputStack.count, tier.smelterMulti)
        return recipeCache
            .getFirstMatch(SingleStackRecipeInput(inputStack), world)
            .flatMap { recipe: T ->
                val resultStack: ItemStack = recipe.getResult(world.registryManager).copy()
                resultStack.count *= processCount
                val output = HTItemResult(resultStack)
                if (!output.canMerge(inventory.getStack(outputIndex))) {
                    return@flatMap DataResult.error { "Failed to merge result into output!" }
                }
                inventory.modifyStack(outputIndex, output::merge)
                inputStack.decrement(processCount)
                DataResult.success(Unit)
            }
    }
}
