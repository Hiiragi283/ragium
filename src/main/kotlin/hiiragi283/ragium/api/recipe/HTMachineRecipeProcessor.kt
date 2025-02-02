package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.extension.canInsert
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper

class HTMachineRecipeProcessor(
    val pos: BlockPos,
    val machine: HTMachineKey,
    val itemHandler: IItemHandler,
    val itemInputs: IntArray,
    val fluidHandler: IFluidHandler,
    val fluidInputs: IntArray,
) {
    val cache: HTMachineRecipeCache = HTMachineRecipeCache.of(machine)

    fun process(level: ServerLevel): Result<Unit> = runCatching {
        val recipe: HTMachineRecipe = cache
            .getFirstMatch(level, pos) {
                itemInputs.map(itemHandler::getStackInSlot).forEach(this::add)
                fluidInputs
                    .map(fluidHandler::getFluidInTank)
                    .forEach(this::add)
            }.getOrThrow()
            .value
        if (!canAccentOutputs(recipe)) throw HTMachineException.MergeResult(false)

        growOutputs(recipe)
        shrinkInputs(recipe)
    }

    private fun canAccentOutputs(recipe: HTMachineRecipe): Boolean {
        // item
        for (output: ItemStack in recipe.getItemOutputs()) {
            if (!itemHandler.canInsert(output)) {
                return false
            }
        }
        // fluid
        for (output: FluidStack in recipe.getFluidOutputs()) {
            if (fluidHandler.fill(output, IFluidHandler.FluidAction.SIMULATE) < output.amount) return false
        }
        return true
    }

    private fun growOutputs(recipe: HTMachineRecipe) {
        // item
        for (output: ItemStack in recipe.getItemOutputs()) {
            ItemHandlerHelper.insertItem(itemHandler, output, false)
        }
        // fluid
        for (output: FluidStack in recipe.getFluidOutputs()) {
            fluidHandler.fill(output, IFluidHandler.FluidAction.EXECUTE)
        }
    }

    private fun shrinkInputs(recipe: HTMachineRecipe) {
        // item
        recipe.itemInputs.forEachIndexed { index: Int, ingredient: SizedIngredient ->
            val stackIn: ItemStack = itemHandler.getStackInSlot(index)
            if (stackIn.`is`(RagiumItems.SLOT_LOCK)) return@forEachIndexed
            stackIn.shrink(ingredient.count())
        }
        // fluid
        recipe.fluidInputs.forEachIndexed { index: Int, ingredient: SizedFluidIngredient ->
            val stackIn: FluidStack = fluidHandler.getFluidInTank(index) ?: return@forEachIndexed
            stackIn.shrink(ingredient.amount())
        }
    }
}
