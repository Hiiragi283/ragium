package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.canFill
import hiiragi283.ragium.api.extension.canInsert
import hiiragi283.ragium.api.extension.insertOrDrop
import hiiragi283.ragium.api.machine.HTMachineException
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler
import java.util.*

/**
 * アイテムまたは液体の完成品を持つレシピのクラス
 */
abstract class HTFluidOutputRecipe(
    group: String,
    protected val itemOutput: Optional<HTItemResult>,
    protected val fluidOutput: Optional<FluidStack>,
) : HTMachineRecipeBase(group) {
    companion object {
        @JvmStatic
        fun <T : HTFluidOutputRecipe> validate(recipe: T): DataResult<T> {
            if (recipe.itemOutput.isEmpty && recipe.fluidOutput.isEmpty) {
                return DataResult.error { "Either item or fluid output required!" }
            }
            return DataResult.success(recipe)
        }
    }

    override val itemResults: List<HTItemResult> = itemOutput.map(::listOf).orElse(listOf())

    fun getFluidOutput(): FluidStack = fluidOutput.orElse(FluidStack.EMPTY).copy()

    /**
     * 指定した[itemHandler]と[fluidHandler]に完成品を入れられるか判定します。
     * @throws HTMachineException 完成品を入れられなかった場合
     */
    fun canInsert(itemHandler: IItemHandler, fluidHandler: IFluidHandler) {
        itemOutput.ifPresent { output: HTItemResult ->
            if (!itemHandler.canInsert(output.getItem(TODO()))) throw HTMachineException.MergeResult(false)
        }
        fluidOutput.ifPresent { output: FluidStack ->
            if (!fluidHandler.canFill(output.copy())) throw HTMachineException.MergeResult(false)
        }
    }

    /**
     * 指定した[itemHandler]と[fluidHandler]に完成品を入れます。
     * @param level 入れられなかった場合にドロップする対象の[net.minecraft.world.level.Level]
     * @param pos 入れられなかった場合にドロップする座標
     */
    fun insertOutputs(
        itemHandler: IItemHandler,
        fluidHandler: IFluidHandler,
        level: Level,
        pos: BlockPos,
    ) {
        itemOutput.ifPresent { output: HTItemResult ->
            itemHandler.insertOrDrop(level, pos.above(), output.getItem(TODO()))
        }
        fluidOutput.ifPresent { output: FluidStack ->
            fluidHandler.fill(output.copy(), IFluidHandler.FluidAction.EXECUTE)
        }
    }
}
