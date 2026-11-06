package hiiragi283.ragium.common.block.entity.processor.base

import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.base.HTLocationToRecipe
import hiiragi283.lib.recipe.handler.cache.HTRecipeCacheHelper
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

abstract class HTLocationToBlockEntity<OUTPUT : Any, RECIPE : HTLocationToRecipe<OUTPUT>>(
    type: BlockEntityType<*>,
    pos: BlockPos,
    state: BlockState,
) : HTProcessorBlockEntity.Energized(type, pos, state) {

    val recipeCache: HTRecipeCacheHelper.Located<RECIPE> = HTRecipeCacheHelper.Located(getLookup())

    protected abstract fun getLookup(): HTRecipeLookup<RECIPE>

    override fun writeValue(output: ValueOutput) {
        super.writeValue(output)
        output.putChild(HTConstants.LAST_RECIPE, recipeCache)
    }

    override fun readValue(input: ValueInput) {
        super.readValue(input)
        input.readChild(HTConstants.LAST_RECIPE, recipeCache)
    }

    private var outputCache: OUTPUT? = null

    override fun setOnlySave() {
        super.setOnlySave()
        outputCache = null
    }

    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        if (!shouldCheck) return false
        // インプットに一致するレシピを探索する
        val recipe: RECIPE = recipeCache.findFirstRecipe(level, pos, createLookupContext()) ?: return run {
            shouldCheck = false
            updateProgress(-1)
            false
        }
        // アウトプットに完成品を搬出できるか判定する
        val output: OUTPUT = outputCache ?: recipe.apply(level, pos)
        outputCache = output
        if (!canProgress) {
            if (canComplete(recipe, output)) {
                canProgress = true
            } else {
                return false
            }
        }
        // レシピの最大進捗量を更新する
        val maxProgress: Int = getMaxRecipeProgress(recipe.getProgressData(level, pos).time)
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
            onComplete(recipe, level, pos, output)
            outputCache = null
        }
        return true
    }

    protected abstract fun canComplete(recipe: RECIPE, output: OUTPUT): Boolean

    protected abstract fun onComplete(recipe: RECIPE, level: ServerLevel, pos: BlockPos, output: OUTPUT)
}
