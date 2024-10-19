package hiiragi283.ragium.api.recipe.machine

import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTRecipeResult
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object HTMachineRecipeProcessorNew {
    @JvmStatic
    fun process(
        world: World,
        pos: BlockPos,
        recipe: HTMachineRecipe?,
        input: HTMachineRecipe.Input,
        inventory: HTSimpleInventory,
    ): Boolean = processInternal(world, pos, recipe, input, inventory)

    @JvmStatic
    private fun processInternal(
        world: World,
        pos: BlockPos,
        recipe: HTMachineRecipe?,
        input: HTMachineRecipe.Input,
        inventory: HTSimpleInventory,
    ): Boolean {
        if (recipe == null) return false
        if (!recipe.matches(input, world)) return false
        if (!canAcceptOutputs(recipe, inventory)) return false
        val machineType: HTMachineType = input.currentType
        val tier: HTMachineTier = input.currentTier
        if (!machineType.getOrDefault(HTMachinePropertyKeys.PROCESSOR_CONDITION)(
                world,
                pos,
                machineType,
                tier,
            )
        ) {
            return false
        }
        modifyOutput(0, recipe, inventory)
        modifyOutput(1, recipe, inventory)
        modifyOutput(2, recipe, inventory)
        decrementInput(0, recipe, inventory)
        decrementInput(1, recipe, inventory)
        decrementInput(2, recipe, inventory)
        return true
    }

    @JvmStatic
    private fun canAcceptOutputs(recipe: HTMachineRecipe, inventory: HTSimpleInventory): Boolean {
        recipe.outputs.forEachIndexed { index: Int, result: HTRecipeResult ->
            val stackIn: ItemStack = inventory.getStack(index + 4)
            if (!result.canAccept(stackIn)) {
                return false
            }
        }
        return true
    }

    @JvmStatic
    private fun modifyOutput(slot: Int, recipe: HTMachineRecipe, inventory: HTSimpleInventory) {
        inventory.modifyStack(slot + 4) { stackIn: ItemStack ->
            recipe.getOutput(slot)?.modifyStack(stackIn) ?: stackIn
        }
    }

    @JvmStatic
    private fun decrementInput(slot: Int, recipe: HTMachineRecipe, inventory: HTSimpleInventory) {
        val delCount: Int = recipe.getInput(slot)?.count ?: return
        inventory.getStack(slot).count -= delCount
    }
}
