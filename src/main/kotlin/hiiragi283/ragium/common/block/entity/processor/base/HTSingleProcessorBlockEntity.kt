package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.handler.lookup.HTSingleRecipeLookupHandler
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

abstract class HTSingleProcessorBlockEntity<INPUT : Any, OUTPUT : Any, RECIPE : HTRecipeFactories.SingleInput<*, INPUT, OUTPUT>>(
    type: BlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTProcessorBlockEntityN.Energized(type, pos, state),
    HTSingleRecipeLookupHandler<INPUT, RECIPE> {
    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putChild(HTConstants.LAST_RECIPE, recipeCache)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.readChild(HTConstants.LAST_RECIPE, recipeCache)
    }

    private var inputCache: INPUT? = null
    private var outputCache: OUTPUT? = null

    override fun setOnlySave() {
        super.setOnlySave()
        inputCache = null
        outputCache = null
    }

    final override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        if (!shouldCheck) return false
        // インプットに一致するレシピを探索する
        val input: INPUT = inputCache ?: getInput()
        inputCache = input
        val recipe: RECIPE = findFirstRecipe(input) ?: return run {
            shouldCheck = false
            inputCache = null
            updateProgress(-1)
            false
        }
        // アウトプットに完成品を搬出できるか判定する
        val output: OUTPUT = outputCache ?: recipe.apply(input)
        outputCache = output
        if (!canProgress) {
            if (canComplete(recipe, input, output)) {
                canProgress = true
            } else {
                return false
            }
        }
        // レシピの最大進捗量を更新する
        val maxProgress: Int = getMaxRecipeProgress(recipe, input)
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
            onComplete(recipe, input, output)
            inputCache = null
            outputCache = null
        }
        return true
    }

    protected abstract fun getInput(): INPUT

    protected abstract fun canComplete(recipe: RECIPE, input: INPUT, output: OUTPUT): Boolean

    protected abstract fun getMaxRecipeProgress(recipe: RECIPE, input: INPUT): Int

    protected abstract fun onComplete(recipe: RECIPE, input: INPUT, output: OUTPUT)
}
