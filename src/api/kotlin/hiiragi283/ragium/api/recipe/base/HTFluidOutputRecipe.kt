package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.extension.canFill
import hiiragi283.ragium.api.extension.canInsert
import hiiragi283.ragium.api.extension.insertOrDrop
import hiiragi283.ragium.api.machine.HTMachineException
import net.minecraft.core.BlockPos
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

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

    /**
     * 指定した[itemHandler]と[fluidHandlers]に完成品を入れられるか判定します。
     * @throws HTMachineException 完成品を入れられなかった場合
     */
    fun canInsert(enchantments: ItemEnchantments, itemHandler: IItemHandler, vararg fluidHandlers: IFluidHandler) {
        for (output: HTItemOutput in itemOutputs) {
            if (!itemHandler.canInsert(output.get())) throw HTMachineException.MergeResult(false)
        }
        for (output: HTFluidOutput in fluidOutputs) {
            if (fluidHandlers.none { it.canFill(output.get()) }) throw HTMachineException.MergeResult(false)
        }
    }

    /**
     * 指定した[itemHandler]と[fluidHandlers]に完成品を入れます。
     * @param level 入れられなかった場合にドロップする対象の[net.minecraft.world.level.Level]
     * @param pos 入れられなかった場合にドロップする座標
     */
    fun insertOutputs(
        level: Level,
        pos: BlockPos,
        enchantments: ItemEnchantments,
        itemHandler: IItemHandler,
        vararg fluidHandlers: IFluidHandler,
    ) {
        for (output: HTItemOutput in itemOutputs) {
            itemHandler.insertOrDrop(level, pos.above(), output.get())
        }
        for (output: HTFluidOutput in fluidOutputs) {
            for (handler: IFluidHandler in fluidHandlers) {
                if (handler.fill(output.get(), IFluidHandler.FluidAction.EXECUTE) > 0) break
            }
        }
    }

    final override fun isValidOutput(): Boolean = itemOutputs.none(HTItemOutput::isNotValid)
}
