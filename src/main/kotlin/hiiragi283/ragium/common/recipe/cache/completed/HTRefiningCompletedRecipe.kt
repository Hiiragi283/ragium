package hiiragi283.ragium.common.recipe.cache.completed

import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.cache.completed.HTCompletedRecipe
import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.ragium.api.recipe.result.HTChemicalResult
import hiiragi283.ragium.common.recipe.HTRefiningRecipe
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTRefiningCompletedRecipe(
    recipe: HTRefiningRecipe,
    private val fluidInputHandler: HTInputHandler<FluidStack>,
    private val itemInputHandler: HTInputHandler<ItemStack>,
    private val firstFluidOutputHandler: HTOutputHandler<FluidStack>,
    private val secondFluidOutputHandler: HTOutputHandler<FluidStack>,
    private val itemOutputHandler: HTOutputHandler<ItemStack>,
) : HTCompletedRecipe.WithProgress<HTRefiningRecipe>(recipe) {
    val output: HTChemicalResult = recipe.assemble(itemInputHandler.getStack(), fluidInputHandler.getStack())

    override fun getProgress(): HTProgressData = HTItemAndFluidRecipeInput(itemInputHandler.getStack(), fluidInputHandler.getStack()).let(recipe::getProgressData)

    override fun canComplete(): Boolean {
        val (item: ItemStack, first: FluidStack, second: FluidStack) = output
        return firstFluidOutputHandler.canInsert(first) && secondFluidOutputHandler.canInsert(second) && itemOutputHandler.canInsert(item)
    }

    override fun complete() {
        val (item: ItemStack, first: FluidStack, second: FluidStack) = output
        // outputs
        firstFluidOutputHandler.canInsert(first)
        secondFluidOutputHandler.canInsert(second)
        itemOutputHandler.canInsert(item)
        // inputs
        recipe.getMatchingStacks(itemInputHandler.getStack(), fluidInputHandler.getStack()).let { (item: ItemStack, fluid: FluidStack) ->
            itemInputHandler.consume(item)
            fluidInputHandler.consume(fluid)
        }
    }
}
