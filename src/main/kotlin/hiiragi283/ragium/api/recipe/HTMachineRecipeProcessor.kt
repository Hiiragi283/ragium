package hiiragi283.ragium.api.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.insert
import hiiragi283.ragium.api.extension.resourceAmount
import hiiragi283.ragium.api.extension.useTransaction
import hiiragi283.ragium.api.fluid.HTMachineFluidStorage
import hiiragi283.ragium.api.fluid.HTSingleFluidStorage
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeEntry
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class HTMachineRecipeProcessor private constructor(
    private val inventory: HTSimpleInventory,
    private val fluidStorage: HTMachineFluidStorage,
    private val typeSize: HTMachineType.Size,
) {
    companion object {
        @JvmStatic
        fun of(inventory: HTSimpleInventory, fluidStorage: HTMachineFluidStorage): HTMachineRecipeProcessor =
            HTMachineRecipeProcessor(inventory, fluidStorage, fluidStorage.typeSize)
    }

    private val outputIndex: Pair<Int, Int> = when (typeSize) {
        HTMachineType.Size.SIMPLE -> 3 to 1
        HTMachineType.Size.LARGE -> 4 to 2
    }

    private val matchGetter: HTRecipeCache<HTMachineInput, HTMachineRecipe> = HTRecipeCache(RagiumRecipeTypes.MACHINE)

    fun process(
        world: World,
        pos: BlockPos,
        machineType: HTMachineType.Processor,
        tier: HTMachineTier,
        input: HTMachineInput,
    ) {
        if (!machineType.isProcessor()) return
        if (inventory.size() != typeSize.invSize) return
        if (fluidStorage.typeSize != typeSize) return
        processInternal(world, pos, machineType, tier, input)
            .ifError {
                machineType.getOrDefault(HTMachinePropertyKeys.PROCESSOR_FAILED)(
                    world,
                    pos,
                    machineType,
                    tier,
                )
            }.ifSuccess {
                machineType.getOrDefault(HTMachinePropertyKeys.PROCESSOR_SUCCEEDED)(
                    world,
                    pos,
                    machineType,
                    tier,
                )
            }
    }

    private fun processInternal(
        world: World,
        pos: BlockPos,
        machineType: HTMachineType.Processor,
        tier: HTMachineTier,
        input: HTMachineInput,
    ): DataResult<HTMachineRecipe> {
        val recipeEntry: RecipeEntry<HTMachineRecipe> = matchGetter
            .getFirstMatch(input, world)
            .getOrNull() ?: return DataResult.error { "Could not find matching recipe!" }
        val recipe: HTMachineRecipe = recipeEntry.value
        if (!canAcceptOutputs(recipe)) return DataResult.error { "Could not insert recipe outputs to slots!" }
        if (!machineType.getOrDefault(HTMachinePropertyKeys.PROCESSOR_CONDITION)(world, pos, machineType, tier)) {
            return DataResult.error { "Not matching required condition!" }
        }
        modifyOutputs(recipe)
        decrementInputs(recipe)
        return DataResult.success(recipe)
    }

    private fun canAcceptOutputs(recipe: HTMachineRecipe): Boolean {
        recipe.itemOutputs.forEachIndexed { index: Int, item: HTRecipeResult.Item ->
            val stackIn: ItemStack = inventory.getStack(index + outputIndex.first)
            if (!item.canMerge(stackIn)) {
                return false
            }
        }
        recipe.fluidOutputs.forEachIndexed { index: Int, fluid: HTRecipeResult.Fluid ->
            val resourceIn: ResourceAmount<FluidVariant> =
                fluidStorage[index + outputIndex.second].resourceAmount
            if (!fluid.canMerge(resourceIn)) {
                return false
            }
        }
        return true
    }

    private fun modifyOutputs(recipe: HTMachineRecipe) {
        recipe.itemOutputs.forEachIndexed { index: Int, item: HTRecipeResult.Item ->
            inventory.modifyStack(index + outputIndex.first, item::merge)
        }
        recipe.fluidOutputs.forEachIndexed { index: Int, fluid: HTRecipeResult.Fluid ->
            val storageIn: HTSingleFluidStorage = fluidStorage[index + outputIndex.second]
            useTransaction { transaction: Transaction ->
                val inserted: Long = storageIn.insert(fluid.resourceAmount, transaction)
                if (inserted > 0) {
                    transaction.commit()
                } else {
                    transaction.abort()
                }
            }
        }
    }

    private fun decrementInputs(recipe: HTMachineRecipe) {
        recipe.itemInputs.forEachIndexed { index: Int, item: HTIngredient.Item ->
            inventory.getStack(index).count -= item.amount
        }
        recipe.fluidInputs.forEachIndexed { index: Int, fluid: HTIngredient.Fluid ->
            useTransaction { transaction: Transaction ->
                val variantIn: FluidVariant = fluidStorage[index].variant
                val extracted: Long = fluidStorage[index].extract(variantIn, fluid.amount, transaction)
                if (extracted > 0) {
                    transaction.commit()
                } else {
                    transaction.abort()
                }
            }
        }
    }
}
