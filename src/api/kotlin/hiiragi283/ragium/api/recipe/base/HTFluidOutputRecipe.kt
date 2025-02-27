package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.machine.HTMachineAccess
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.storage.HTFluidTank
import hiiragi283.ragium.api.storage.HTItemSlot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * アイテムまたは液体の完成品を持つレシピのクラス
 */
abstract class HTFluidOutputRecipe(group: String, val itemOutputs: List<HTItemOutput>, val fluidOutputs: List<HTFluidOutput>) :
    HTMachineRecipeBase(group) {
    companion object {
        @JvmStatic
        fun <T : HTFluidOutputRecipe> validate(recipe: T): DataResult<T> {
            if (recipe.itemOutputs.isEmpty() && recipe.fluidOutputs.isEmpty()) {
                return DataResult.error { "Either item or fluid output required!" }
            }
            return DataResult.success(recipe)
        }
    }

    fun canInsert(machine: HTMachineAccess, itemSlots: IntArray, fluidSlots: IntArray) {
        // Item
        for (index: Int in itemOutputs.indices) {
            val output: ItemStack = itemOutputs[index].get()
            val slot: HTItemSlot = itemSlots.getOrNull(index)?.let(machine::getItemSlot) ?: return
            if (!slot.canInsert(output)) throw HTMachineException.MergeOutput(false)
        }
        // Fluid
        for (index: Int in fluidOutputs.indices) {
            val output: FluidStack = fluidOutputs[index].get()
            val tank: HTFluidTank = fluidSlots.getOrNull(index)?.let(machine::getFluidTank) ?: return
            if (!tank.canFill(output)) throw HTMachineException.MergeOutput(false)
        }
    }

    fun insertOutputs(machine: HTMachineAccess, itemSlots: IntArray, fluidSlots: IntArray) {
        // Item
        for (index: Int in itemOutputs.indices) {
            val output: ItemStack = itemOutputs[index].get()
            val slot: HTItemSlot = itemSlots.getOrNull(index)?.let(machine::getItemSlot) ?: return
            slot.insertItem(output, false)
        }
        // Fluid
        for (index: Int in fluidOutputs.indices) {
            val output: FluidStack = fluidOutputs[index].get()
            val tank: HTFluidTank = fluidSlots.getOrNull(index)?.let(machine::getFluidTank) ?: return
            tank.fill(output, IFluidHandler.FluidAction.EXECUTE)
        }
    }
}
