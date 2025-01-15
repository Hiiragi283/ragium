package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.block.entity.HTBlockEntityHandlerProvider
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineProvider
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
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.templates.EmptyFluidHandler
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.wrapper.EmptyItemHandler

class HTMachineRecipeProcessor(
    val machine: HTMachineKey,
    val itemHandler: IItemHandler,
    val itemInputs: IntArray,
    val itemOutputs: IntArray,
    val catalystIndex: Int,
    val fluidHandler: IFluidHandler,
    val fluidInputs: IntArray,
) : HTRecipeProcessor {
    companion object {
        @JvmStatic
        fun <T> fromMachine(
            blockEntity: T,
            itemInputs: IntArray,
            itemOutputs: IntArray,
            catalystIndex: Int,
            fluidInputs: IntArray,
        ): HTMachineRecipeProcessor where T : HTMachineProvider, T : HTBlockEntityHandlerProvider = HTMachineRecipeProcessor(
            blockEntity.machineKey,
            blockEntity.getItemHandler(null) ?: EmptyItemHandler.INSTANCE,
            itemInputs,
            itemOutputs,
            catalystIndex,
            blockEntity.getFluidHandler(null) ?: EmptyFluidHandler.INSTANCE,
            fluidInputs,
        )
    }

    val cache: HTRecipeCache<HTMachineInput, HTMachineRecipe> = HTRecipeCache(RagiumRecipes.MACHINE_TYPE)

    override fun process(level: ServerLevel, tier: HTMachineTier): Result<Unit> = runCatching {
        val input: HTMachineInput = HTMachineInput.create(machine, tier) {
            itemInputs.map(itemHandler::getStackInSlot).forEach(this::add)
            fluidInputs.map(fluidHandler::getFluidInTank).forEach(this::add)
            catalyst = itemHandler.getStackInSlot(catalystIndex)
        }
        val recipe: HTMachineRecipe = cache.getFirstMatch(input, level).getOrThrow()
        if (!canAccentOutputs(recipe)) throw HTMachineException.MergeResult(false)

        shrinkInputs(recipe)
    }

    private fun canAccentOutputs(recipe: HTMachineRecipe): Boolean {
        // item
        itemOutputs.forEachIndexed { index: Int, slot: Int ->
            val itemOutput: ItemStack = recipe.itemOutputs.getOrNull(index) ?: return@forEachIndexed
            if (!itemHandler.insertItem(slot, itemOutput, true).isEmpty) return false
        }
        // fluid
        for (stack: FluidStack in recipe.fluidOutputs) {
            if (fluidHandler.fill(stack, IFluidHandler.FluidAction.SIMULATE) < stack.amount) return false
        }
        return true
    }

    private fun growOutputs(recipe: HTMachineRecipe) {
        // item
        itemOutputs.forEachIndexed { index: Int, slot: Int ->
            val itemOutput: ItemStack = recipe.itemOutputs.getOrNull(index) ?: return@forEachIndexed
            itemHandler.insertItem(slot, itemOutput, false)
        }
        // fluid
        for (stack: FluidStack in recipe.fluidOutputs) {
            fluidHandler.fill(stack, IFluidHandler.FluidAction.EXECUTE)
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
            val stackIn: FluidStack = fluidHandler.getFluidInTank(index)
            stackIn.shrink(ingredient.amount())
        }
    }
}
