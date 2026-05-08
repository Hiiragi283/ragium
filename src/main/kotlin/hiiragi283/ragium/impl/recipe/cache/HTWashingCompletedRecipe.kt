package hiiragi283.ragium.impl.recipe.cache

import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.impl.recipe.cache.completed.HTCompletedRecipe
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTWashingCompletedRecipe(
    recipe: HTWashingRecipe,
    private val inputHandler: HTInputHandler<ItemStack>,
    private val waterHandler: HTInputHandler<FluidStack>,
    private val outputHandler: HTOutputHandler<ItemStack>,
) : HTCompletedRecipe.WithProgress<HTWashingRecipe>(recipe) {
    private val outputs: Iterable<ItemStack> = recipe.assemble(inputHandler.getStack())

    override fun getProgress(): HTProgressData = HTItemAndFluidRecipeInput(inputHandler.getStack(), waterHandler.getStack()).let(recipe::getProgressData)

    override fun canComplete(): Boolean = outputs.all(outputHandler::canInsert)

    override fun complete() {
        // outputs
        outputs.forEach(outputHandler::insert)
        // inputs
        recipe.getRequiredAmount(inputHandler.getStack(), waterHandler.getStack()).let { (item: Int, fluid: Int) ->
            inputHandler.consume(item)
            waterHandler.consume(fluid)
        }
    }
}
