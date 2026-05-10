package hiiragi283.ragium.impl.recipe.cache.completed

import hiiragi283.core.api.recipe.handler.HTInputHandler
import hiiragi283.core.api.recipe.handler.HTOutputHandler
import hiiragi283.core.impl.recipe.cache.completed.HTCompletedRecipe
import hiiragi283.ragium.api.recipe.base.HTEnchantingRecipe
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

class HTEnchantingCompletedRecipe(
    recipe: HTEnchantingRecipe,
    private val baseInputHandler: HTInputHandler<ItemStack>,
    private val additionInputHandler: HTInputHandler<ItemStack>,
    private val expFluidInputHandler: HTInputHandler<FluidStack>,
    private val outputHandler: HTOutputHandler<ItemStack>,
) : HTCompletedRecipe<HTEnchantingRecipe>(recipe) {
    private val output: ItemStack = recipe.assemble(
        baseInputHandler.getStack(),
        additionInputHandler.getStack(),
        expFluidInputHandler.getAmount(),
    )

    override fun canComplete(): Boolean = outputHandler.canInsert(output)

    override fun complete() {
        // output
        outputHandler.insert(output)
        // inputs
        recipe
            .getRequiredAmount(baseInputHandler.getStack(), additionInputHandler.getStack(), expFluidInputHandler.getAmount())
            .let { (base: Int, addition: Int, expAmount: Int) ->
                baseInputHandler.consume(base)
                additionInputHandler.consume(addition)
                expFluidInputHandler.consume(expAmount)
            }
    }
}
