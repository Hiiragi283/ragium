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

abstract class HTDoubleProcessorBlockEntity<IN_A : Any, IN_B : Any, OUT : Any, R : HTRecipeFactories.DoubleInputLike<*, IN_A, IN_B, OUT>>(
    type: BlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTProcessorBlockEntity.Energized(type, pos, state),
    HTDoubleRecipeLookupHandler<IN_A, IN_B, R> {
    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putChild(HTConstants.LAST_RECIPE, recipeCache)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.readChild(HTConstants.LAST_RECIPE, recipeCache)
    }

    private var firstInputCache: IN_A? = null
    private var secondInputCache: IN_B? = null
    private var outputCache: OUT? = null

    override fun setOnlySave() {
        super.setOnlySave()
        firstInputCache = null
        secondInputCache = null
        outputCache = null
    }

    final override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        if (!shouldCheck) return false
        // インプットに一致するレシピを探索する
        val firstInput: IN_A = firstInputCache ?: getFirstInput()
        val secondInput: IN_B = secondInputCache ?: getSecondInput()
        firstInputCache = firstInput
        secondInputCache = secondInput
        val recipe: R = findFirstRecipe(firstInput, secondInput) ?: return run {
            shouldCheck = false
            firstInputCache = null
            secondInputCache = null
            updateProgress(-1)
            false
        }
        // アウトプットに完成品を搬出できるか判定する
        val output: OUT = outputCache ?: recipe.apply(firstInput, secondInput)
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

    protected abstract fun getFirstInput(): IN_A

    protected abstract fun getSecondInput(): IN_B

    protected abstract fun canComplete(recipe: R, first: IN_A, second: IN_B, output: OUT): Boolean

    protected abstract fun getMaxRecipeProgress(recipe: R, first: IN_A, second: IN_B): Int

    protected abstract fun onComplete(recipe: R, first: IN_A, second: IN_B, output: OUT)
}
