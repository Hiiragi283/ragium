package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.HTRecipeProcessor
import hiiragi283.ragium.common.init.RagiumRecipes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.IFluidTank
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import net.neoforged.neoforge.items.IItemHandler
import java.util.function.Function

class HTMachineRecipeProcessor(
    val machine: HTMachineKey,
    val itemHandler: IItemHandler,
    val itemInputs: IntArray,
    val itemOutputs: IntArray,
    val catalystIndex: Int,
    val fluidTanks: Function<Int, out IFluidTank?>,
    val fluidInputs: IntArray,
    val fluidOutputs: IntArray,
) : HTRecipeProcessor {
    val cache: HTRecipeCache<HTMachineInput, HTMachineRecipe> = HTRecipeCache(RagiumRecipes.MACHINE_TYPE)

    override fun process(level: ServerLevel, tier: HTMachineTier): Result<Unit> = runCatching {
        val input: HTMachineInput = HTMachineInput.create(machine, tier) {
            itemInputs.map(itemHandler::getStackInSlot).forEach(this::add)
            fluidInputs
                .map(fluidTanks::apply)
                .filterNotNull()
                .map(IFluidTank::getFluid)
                .forEach(this::add)
            catalyst = itemHandler.getStackInSlot(catalystIndex)
        }
        val recipe: HTMachineRecipe = cache.getFirstMatch(input, level).getOrThrow()
        if (!canAccentOutputs(recipe)) throw HTMachineException.MergeResult(false)

        shrinkInputs(recipe)
    }

    private fun canAccentOutputs(recipe: HTMachineRecipe): Boolean {
        // item
        itemOutputs.forEachIndexed { index: Int, slot: Int ->
            val itemOutput: ItemStack = recipe.getItemOutput(index) ?: return@forEachIndexed
            if (!itemHandler.insertItem(slot, itemOutput, true).isEmpty) return false
        }
        // fluid
        fluidOutputs.forEachIndexed { index: Int, slot: Int ->
            val fluidOutput: FluidStack = recipe.getFluidOutput(index) ?: return@forEachIndexed
            val tank: IFluidTank = fluidTanks.apply(slot) ?: return@forEachIndexed
            if (tank.fill(fluidOutput, IFluidHandler.FluidAction.SIMULATE) < fluidOutput.amount) return false
        }
        return true
    }

    private fun growOutputs(recipe: HTMachineRecipe) {
        // item
        itemOutputs.forEachIndexed { index: Int, slot: Int ->
            val itemOutput: ItemStack = recipe.getItemOutput(index) ?: return@forEachIndexed
            itemHandler.insertItem(slot, itemOutput, false)
        }
        // fluid
        fluidOutputs.forEachIndexed { index: Int, slot: Int ->
            val fluidOutput: FluidStack = recipe.getFluidOutput(index) ?: return@forEachIndexed
            val tank: IFluidTank = fluidTanks.apply(slot) ?: return@forEachIndexed
            tank.fill(fluidOutput, IFluidHandler.FluidAction.EXECUTE)
        }
    }

    private fun shrinkInputs(recipe: HTMachineRecipe) {
        // item
        recipe.itemInputs.forEachIndexed { index: Int, ingredient: SizedIngredient ->
            val stackIn: ItemStack = itemHandler.getStackInSlot(index)
            stackIn.shrink(ingredient.count())
        }
        // fluid
        recipe.fluidInputs.forEachIndexed { index: Int, ingredient: SizedFluidIngredient ->
            val stackIn: FluidStack = fluidTanks.apply(index)?.fluid ?: return@forEachIndexed
            stackIn.shrink(ingredient.amount())
        }
    }
}
