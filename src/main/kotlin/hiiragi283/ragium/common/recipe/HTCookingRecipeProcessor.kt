package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeProcessor
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.items.IItemHandler
import java.util.function.Supplier

class HTCookingRecipeProcessor(
    val handler: IItemHandler,
    val inputIndex: Int,
    val outputIndex: Int,
    recipeType: Supplier<RecipeType<out AbstractCookingRecipe>>,
) : HTRecipeProcessor {
    val cache: HTRecipeCache<SingleRecipeInput, out AbstractCookingRecipe> = HTRecipeCache(recipeType.get())

    override fun process(level: ServerLevel, tier: HTMachineTier): Result<Unit> = runCatching {
        val inputStack: ItemStack = handler.getStackInSlot(inputIndex)
        val input = SingleRecipeInput(inputStack)
        val recipe: AbstractCookingRecipe = cache.getFirstMatch(input, level).getOrThrow()
        val output: ItemStack = recipe.getResultItem(level.registryAccess()).copy()
        if (handler.insertItem(outputIndex, output, true).isEmpty) {
            handler.insertItem(outputIndex, output, false)
            inputStack.shrink(1)
        } else {
            throw HTMachineException.MergeResult(true)
        }
    }
}
