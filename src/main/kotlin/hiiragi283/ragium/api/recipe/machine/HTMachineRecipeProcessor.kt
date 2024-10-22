package hiiragi283.ragium.api.recipe.machine

import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object HTMachineRecipeProcessor {
    @JvmStatic
    fun process(
        world: World,
        pos: BlockPos,
        recipe: HTMachineRecipeNew<*>?,
        definition: HTMachineDefinition,
        inventory: HTSimpleInventory,
    ): Boolean = processInternal(world, pos, recipe, definition, inventory)

    @JvmStatic
    private fun processInternal(
        world: World,
        pos: BlockPos,
        recipe: HTMachineRecipeNew<*>?,
        definition: HTMachineDefinition,
        inventory: HTSimpleInventory,
    ): Boolean {
        if (recipe == null) return false
        if (!canAcceptOutputs(recipe, inventory)) return false
        val (type: HTMachineType, tier: HTMachineTier) = definition
        if (!type.getOrDefault(HTMachinePropertyKeys.PROCESSOR_CONDITION)(
                world,
                pos,
                type,
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
    private fun canAcceptOutputs(recipe: HTMachineRecipeNew<*>, inventory: HTSimpleInventory): Boolean {
        /*recipe.outputs.forEachIndexed { index: Int, result: HTRecipeResult ->
            val stackIn: ItemStack = inventory.getStack(index + 4)
            if (!result.canAccept(stackIn)) {
                return false
            }
        }*/
        return true
    }

    @JvmStatic
    private fun modifyOutput(slot: Int, recipe: HTMachineRecipeNew<*>, inventory: HTSimpleInventory) {
        /*inventory.modifyStack(slot + 2) { stackIn: ItemStack ->
            recipe.getOutput(slot)?.modifyStack(stackIn) ?: stackIn
        }*/
    }

    @JvmStatic
    private fun decrementInput(slot: Int, recipe: HTMachineRecipeNew<*>, inventory: HTSimpleInventory) {
        /*val delCount: Int = recipe.getInput(slot)?.count ?: return
        inventory.getStack(slot).count -= delCount*/
    }
}
