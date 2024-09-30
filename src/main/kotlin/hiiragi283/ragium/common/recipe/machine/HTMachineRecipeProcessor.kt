package hiiragi283.ragium.common.recipe.machine

import com.mojang.serialization.DataResult
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.inventory.HTSimpleInventory
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.recipe.HTRecipeBase
import hiiragi283.ragium.common.recipe.HTRecipeResult
import hiiragi283.ragium.common.recipe.HTRequireScanRecipe
import hiiragi283.ragium.common.world.HTDataDriveManager
import hiiragi283.ragium.common.world.dataDriveManager
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeEntry
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class HTMachineRecipeProcessor(val inventory: HTSimpleInventory) {
    init {
        check(inventory.size() == 7)
    }

    fun process(
        world: World,
        pos: BlockPos,
        machineType: HTMachineType.Processor,
        tier: HTMachineTier,
    ) {
        processInternal(world, pos, machineType, tier)
            .ifError { machineType.onFailed(world, pos, machineType, tier) }
            .ifSuccess { machineType.onSucceeded(world, pos, machineType, tier) }
    }

    private fun processInternal(
        world: World,
        pos: BlockPos,
        machineType: HTMachineType.Processor,
        tier: HTMachineTier,
    ): DataResult<HTRecipeBase<HTMachineRecipe.Input>> {
        val processorType: HTMachineType.Processor = machineType.asProcessor() ?: return DataResult.error { "" }
        val input: HTMachineRecipe.Input =
            HTMachineRecipe.Input.create(
                processorType,
                tier,
                inventory.getStack(0),
                inventory.getStack(1),
                inventory.getStack(2),
                inventory.getStack(3),
            )
        val recipeEntry: RecipeEntry<HTMachineRecipe> = world.recipeManager
            .getFirstMatch(RagiumRecipeTypes.MACHINE, input, world)
            .getOrNull() ?: return DataResult.error { "Could not find matching recipe!" }
        val recipe: HTRecipeBase<HTMachineRecipe.Input> = recipeEntry.value
        val recipeId: Identifier = recipeEntry.id
        if (!canAcceptOutputs(recipe)) return DataResult.error { "Could not insert recipe outputs to slots!" }
        if (!machineType.match(world, pos, machineType, tier)) return DataResult.error { "Not matching required condition!" }
        if ((recipe as? HTRequireScanRecipe)?.requireScan == true) {
            val manager: HTDataDriveManager = world.dataDriveManager
                ?: return DataResult.error { "The recipe; $recipeId requires scanning but data drive manager not found!" }
            if (recipeId !in manager) return DataResult.error { "The recipe; $recipeId is locked!" }
        }
        modifyOutput(0, recipe)
        modifyOutput(1, recipe)
        modifyOutput(2, recipe)
        decrementInput(0, recipe)
        decrementInput(1, recipe)
        decrementInput(2, recipe)
        return DataResult.success(recipe)
    }

    private fun canAcceptOutputs(recipe: HTRecipeBase<HTMachineRecipe.Input>): Boolean {
        recipe.outputs.forEachIndexed { index: Int, result: HTRecipeResult ->
            val stackIn: ItemStack = inventory.getStack(index + 4)
            if (!result.canAccept(stackIn)) {
                return false
            }
        }
        return true
    }

    private fun modifyOutput(slot: Int, recipe: HTRecipeBase<HTMachineRecipe.Input>) {
        inventory.modifyStack(slot + 4) { stackIn: ItemStack ->
            recipe.getOutput(slot)?.modifyStack(stackIn) ?: stackIn
        }
    }

    private fun decrementInput(slot: Int, recipe: HTRecipeBase<HTMachineRecipe.Input>) {
        val delCount: Int = recipe.getInput(slot)?.count ?: return
        inventory.getStack(slot).count -= delCount
    }
}
