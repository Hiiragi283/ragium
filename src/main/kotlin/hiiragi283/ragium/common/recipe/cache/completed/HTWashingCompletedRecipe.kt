package hiiragi283.ragium.common.recipe.cache.completed

import hiiragi283.core.api.recipe.cache.completed.HTCompletedRecipe
import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.ragium.common.recipe.HTWashingRecipe
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack

class HTWashingCompletedRecipe(
    recipe: HTWashingRecipe,
    private val inputHandler: HTInputHandler<ItemStack>,
    private val waterHandler: HTInputHandler<FluidStack>,
    private val outputHandler: HTOutputHandler<ItemStack>,
) : HTCompletedRecipe.WithProgress<SingleRecipeInput, HTWashingRecipe>(recipe) {
    private val outputs: Iterable<ItemStack> = recipe.assemble(input)

    override fun createInput(): SingleRecipeInput = SingleRecipeInput(inputHandler.getStack())

    override fun canComplete(): Boolean = outputs.all(outputHandler::canInsert)

    override fun complete() {
        // outputs
        outputs.forEach(outputHandler::insert)
        // inputs
        recipe.getMatchingStacks(inputHandler.getStack(), waterHandler.getStack()).let { (item: ItemStack, fluid: FluidStack) ->
            inputHandler.consume(item)
            waterHandler.consume(fluid)
        }
    }
}
