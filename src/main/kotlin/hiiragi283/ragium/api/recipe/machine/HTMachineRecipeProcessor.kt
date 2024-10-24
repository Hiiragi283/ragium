package hiiragi283.ragium.api.recipe.machine

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.dataDriveManager
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTRecipeBase
import hiiragi283.ragium.api.recipe.HTRecipeResult
import hiiragi283.ragium.api.recipe.HTRequireScanRecipe
import hiiragi283.ragium.api.world.HTDataDriveManager
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeEntry
import net.minecraft.recipe.RecipeManager
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.input.RecipeInput
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class HTMachineRecipeProcessor<I : RecipeInput, T : HTRecipeBase<I>>(
    private val inventory: HTSimpleInventory,
    recipeType: RecipeType<T>,
    private val inputFactory: (HTMachineType, HTMachineTier, HTSimpleInventory) -> I,
) {
    private val matchGetter: RecipeManager.MatchGetter<I, T> = RecipeManager.createCachedMatchGetter(recipeType)

    fun process(
        world: World,
        pos: BlockPos,
        machineType: HTMachineType,
        tier: HTMachineTier,
    ) {
        if (!machineType.isProcessor()) return
        if (inventory.size() != 7) return
        processInternal(world, pos, machineType, tier)
            .ifError { machineType.getOrDefault(HTMachinePropertyKeys.PROCESSOR_FAILED)(world, pos, machineType, tier) }
            .ifSuccess { machineType.getOrDefault(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED)(world, pos, machineType, tier) }
    }

    private fun processInternal(
        world: World,
        pos: BlockPos,
        machineType: HTMachineType,
        tier: HTMachineTier,
    ): DataResult<T> {
        val recipeEntry: RecipeEntry<T> = matchGetter
            .getFirstMatch(inputFactory(machineType, tier, inventory), world)
            .getOrNull() ?: return DataResult.error { "Could not find matching recipe!" }
        val recipe: T = recipeEntry.value
        val recipeId: Identifier = recipeEntry.id
        if (!canAcceptOutputs(recipe)) return DataResult.error { "Could not insert recipe outputs to slots!" }
        if (!machineType.getOrDefault(HTMachinePropertyKeys.PROCESSOR_CONDITION)(world, pos, machineType, tier)) {
            return DataResult.error { "Not matching required condition!" }
        }
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

    private fun canAcceptOutputs(recipe: T): Boolean {
        recipe.outputs.forEachIndexed { index: Int, result: HTRecipeResult ->
            val stackIn: ItemStack = inventory.getStack(index + 4)
            if (!result.canAccept(stackIn)) {
                return false
            }
        }
        return true
    }

    private fun modifyOutput(slot: Int, recipe: T) {
        inventory.modifyStack(slot + 4) { stackIn: ItemStack ->
            recipe.getOutput(slot)?.modifyStack(stackIn) ?: stackIn
        }
    }

    private fun decrementInput(slot: Int, recipe: T) {
        val delCount: Int = recipe.getInput(slot)?.count ?: return
        inventory.getStack(slot).count -= delCount
    }
}
