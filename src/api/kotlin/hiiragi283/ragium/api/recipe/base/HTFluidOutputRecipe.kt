package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.storage.HTStorageIO
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import java.util.*

/**
 * アイテムまたは液体の完成品を持つレシピのクラス
 */
abstract class HTFluidOutputRecipe(group: String, val itemOutputs: List<HTItemOutput>, val fluidOutputs: List<HTFluidOutput>) :
    HTMachineRecipe(group) {
    companion object {
        @JvmStatic
        fun <T : HTFluidOutputRecipe> validate(recipe: T): DataResult<T> {
            if (recipe.itemOutputs.isEmpty() && recipe.fluidOutputs.isEmpty()) {
                return DataResult.error { "Either item or fluid output required!" }
            }
            return DataResult.success(recipe)
        }
    }

    fun getItemOutput(index: Int): Optional<HTItemOutput> = Optional.ofNullable(itemOutputs.getOrNull(index))

    fun getFluidOutput(index: Int): Optional<HTFluidOutput> = Optional.ofNullable(fluidOutputs.getOrNull(index))

    protected fun validateItemOutput(context: HTMachineRecipeContext, index: Int) {
        getItemOutput(index).ifPresent { output: HTItemOutput ->
            if (!context.getSlot(HTStorageIO.OUTPUT, index).canInsert(output.get())) {
                throw HTMachineException.GrowItem()
            }
        }
    }

    protected fun validateFluidOutput(context: HTMachineRecipeContext, index: Int) {
        getFluidOutput(index).ifPresent { output: HTFluidOutput ->
            if (!context.getTank(HTStorageIO.OUTPUT, index).canFill(output.get())) {
                throw HTMachineException.GrowFluid()
            }
        }
    }

    protected fun processItemOutput(context: HTMachineRecipeContext, index: Int) {
        getItemOutput(index).ifPresent { output: HTItemOutput ->
            context.getSlot(HTStorageIO.OUTPUT, index).insertItem(output.get(), false)
        }
    }

    protected fun processFluidOutput(context: HTMachineRecipeContext, index: Int) {
        getFluidOutput(index).ifPresent { output: HTFluidOutput ->
            context.getTank(HTStorageIO.OUTPUT, index).fill(output.get(), IFluidHandler.FluidAction.EXECUTE)
        }
    }
}
