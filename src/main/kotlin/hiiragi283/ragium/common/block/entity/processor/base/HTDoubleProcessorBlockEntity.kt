package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.handler.lookup.HTDoubleRecipeLookupHandler
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

/**
 * @see hiiragi283.lib.recipe.handler.HTDoubleRecipeHandler
 */
abstract class HTDoubleProcessorBlockEntity<INPUT_A : Any, INPUT_B : Any, OUTPUT : Any, RECIPE : HTRecipeFactories.DoubleInput<*, INPUT_A, INPUT_B, OUTPUT>>(
    type: BlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTProcessorBlockEntityN.Energized(type, pos, state),
    HTDoubleRecipeLookupHandler<INPUT_A, INPUT_B, RECIPE> {
    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putChild(HTConstants.LAST_RECIPE, recipeCache)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.readChild(HTConstants.LAST_RECIPE, recipeCache)
    }

    private var firstInputCache: INPUT_A? = null
    private var secondInputCache: INPUT_B? = null
    private var outputCache: OUTPUT? = null

    override fun setOnlySave() {
        super.setOnlySave()
        firstInputCache = null
        secondInputCache = null
        outputCache = null
    }

    final override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        if (!shouldCheck) return false
        // インプットに一致するレシピを探索する
        val firstInput: INPUT_A = firstInputCache ?: getFirstInput()
        val secondInput: INPUT_B = secondInputCache ?: getSecondInput()
        firstInputCache = firstInput
        secondInputCache = secondInput
        val recipe: RECIPE = findFirstRecipe(firstInput, secondInput) ?: return run {
            shouldCheck = false
            firstInputCache = null
            secondInputCache = null
            updateProgress(-1)
            false
        }
        // アウトプットに完成品を搬出できるか判定する
        val output: OUTPUT = outputCache ?: recipe.apply(firstInput, secondInput)
        outputCache = output
        if (!canProgress) {
            if (canComplete(recipe, firstInput, secondInput, output)) {
                canProgress = true
            } else {
                return false
            }
        }
        // レシピの最大進捗量を更新する
        val maxProgress: Int = getMaxRecipeProgress(recipe, firstInput, secondInput)
        if (this.maxProgress != maxProgress) {
            updateProgress(maxProgress)
        }
        // 進捗を更新する
        if (progress < maxProgress) {
            progress += getRecipeProgress()
        }
        // 進捗が最大量を超えたらレシピを実行する
        if (progress >= maxProgress) {
            progress -= maxProgress
            canProgress = false
            onComplete(recipe, firstInput, secondInput, output)
            firstInputCache = null
            secondInputCache = null
            outputCache = null
        }
        return true
    }

    protected abstract fun getFirstInput(): INPUT_A

    protected abstract fun getSecondInput(): INPUT_B

    protected abstract fun canComplete(recipe: RECIPE, first: INPUT_A, second: INPUT_B, output: OUTPUT): Boolean

    protected abstract fun getMaxRecipeProgress(recipe: RECIPE, first: INPUT_A, second: INPUT_B): Int

    protected abstract fun onComplete(recipe: RECIPE, first: INPUT_A, second: INPUT_B, output: OUTPUT)
}
