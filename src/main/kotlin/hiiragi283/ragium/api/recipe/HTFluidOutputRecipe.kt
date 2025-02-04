package hiiragi283.ragium.api.recipe

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.canFill
import hiiragi283.ragium.api.extension.canInsert
import hiiragi283.ragium.api.extension.insertOrDrop
import hiiragi283.ragium.api.machine.HTMachineException
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler
import java.util.*

abstract class HTFluidOutputRecipe(private val group: String, val itemOutput: Optional<ItemStack>, val fluidOutput: Optional<FluidStack>) :
    HTMachineRecipeBase {
    companion object {
        @JvmStatic
        fun <T : HTFluidOutputRecipe> validate(recipe: T): DataResult<T> {
            if (recipe.itemOutput.isEmpty && recipe.fluidOutput.isEmpty) {
                return DataResult.error { "Either item or fluid output required!" }
            }
            return DataResult.success(recipe)
        }
    }

    fun canInsert(itemHandler: IItemHandler, fluidHandler: IFluidHandler) {
        itemOutput.ifPresent { output: ItemStack ->
            if (!itemHandler.canInsert(output)) throw HTMachineException.MergeResult(false)
        }
        fluidOutput.ifPresent { output: FluidStack ->
            if (!fluidHandler.canFill(output)) throw HTMachineException.MergeResult(false)
        }
    }

    fun insertOutputs(
        itemHandler: IItemHandler,
        fluidHandler: IFluidHandler,
        level: Level,
        pos: BlockPos,
    ) {
        itemOutput.ifPresent { output: ItemStack ->
            itemHandler.insertOrDrop(level, pos.above(), output)
        }
        fluidOutput.ifPresent { output: FluidStack ->
            fluidHandler.fill(output, IFluidHandler.FluidAction.EXECUTE)
        }
    }

    final override fun assemble(input: HTRecipeInput, registries: HolderLookup.Provider): ItemStack = getResultItem(registries)

    final override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = itemOutput.orElse(ItemStack.EMPTY).copy()

    final override fun getGroup(): String = group
}
