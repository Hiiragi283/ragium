package hiiragi283.lib.recipe.handler

import hiiragi283.lib.recipe.HTRecipeFactory
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.RecipeInput

abstract class HTRecipeHandler<INPUT : RecipeInput, OUTPUT : Any, RECIPE : HTRecipeFactory<INPUT, OUTPUT>> {
    var progress: Int = 0
    var maxProgress: Int = 0

    private fun updateProgress(maxProgress: Int) {
        this.maxProgress = maxProgress
        progress = 0
    }

    private var shouldCheck: Boolean = true
    private var canProgress: Boolean = false

    private var inputCache: INPUT? = null
    private var outputCache: OUTPUT? = null

    fun createListener(listener: Runnable): Runnable = Runnable {
        shouldCheck = true
        inputCache = null
        outputCache = null
        listener.run()
    }

    fun tick(level: ServerLevel): Boolean {
        if (!shouldCheck) return false
        // インプットに一致するレシピを探索する
        val input: INPUT = createInput()
        inputCache = input
        val recipe: RECIPE = findRecipe(level, input) ?: return run {
            shouldCheck = false
            inputCache = null
            updateProgress(-1)
            false
        }

        // アウトプットに完成品を搬出できるか判定する
        if (!canProgress) {
            val output: OUTPUT = recipe.produce(input)
            outputCache = output
            if (canComplete(recipe, input, output)) {
                canProgress = true
            } else {
                return false
            }
        }
        // レシピの最大進捗量を更新する
        val maxProgress: Int = getMaxProgress(recipe, input)
        if (this.maxProgress != maxProgress) {
            updateProgress(maxProgress)
        }
        // 進捗を更新する
        if (progress < maxProgress) {
            progress += getProgress()
        }
        // 進捗が最大量を超えたらレシピを実行する
        if (progress >= maxProgress) {
            progress -= maxProgress
            canProgress = false
            onComplete(recipe, inputCache!!, outputCache!!)
            inputCache = null
            outputCache = null
        }
        return true
    }

    protected abstract fun createInput(): INPUT

    protected abstract fun findRecipe(level: ServerLevel, input: INPUT): RECIPE?

    protected abstract fun canComplete(recipe: RECIPE, input: INPUT, output: OUTPUT): Boolean

    protected abstract fun getMaxProgress(recipe: RECIPE, input: INPUT): Int

    protected abstract fun getProgress(): Int

    protected abstract fun onComplete(recipe: RECIPE, input: INPUT, output: OUTPUT)
}
