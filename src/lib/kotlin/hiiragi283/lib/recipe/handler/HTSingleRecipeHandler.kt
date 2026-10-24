package hiiragi283.lib.recipe.handler

import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.handler.cache.HTSingleRecipeCache
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import java.util.function.Function
import java.util.function.Predicate

/**
 * 単一の入力に対する[HTRecipeHandler]の拡張クラスです。
 * @param INPUT レシピの入力となるクラス
 * @param OUTPUT レシピの出力となるクラス
 * @param RECIPE 提供するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
abstract class HTSingleRecipeHandler<INPUT : Any, OUTPUT : Any, RECIPE : Function<INPUT, OUTPUT>> : HTRecipeHandler() {
    protected val progressor: Progressor<INPUT, RECIPE> = createProgressor()

    protected abstract fun createProgressor(): Progressor<INPUT, RECIPE>

    private var inputCache: INPUT? = null
    private var outputCache: OUTPUT? = null

    override fun run() {
        super.run()
        inputCache = null
        outputCache = null
    }

    override fun tick(level: ServerLevel): Boolean {
        if (!shouldCheck) return false
        // インプットに一致するレシピを探索する
        val input: INPUT = getInput()
        inputCache = input
        val recipe: RECIPE = findRecipe(input, level) ?: return run {
            shouldCheck = false
            inputCache = null
            updateProgress(-1)
            false
        }
        // アウトプットに完成品を搬出できるか判定する
        val output: OUTPUT = recipe.apply(input)
        outputCache = output
        if (!canProgress) {
            if (canComplete(recipe, input, output)) {
                canProgress = true
            } else {
                return false
            }
        }
        // レシピの最大進捗量を更新する
        val maxProgress: Int = progressor.getMaxProgress(recipe, input)
        if (this.maxProgress != maxProgress) {
            updateProgress(maxProgress)
        }
        // 進捗を更新する
        if (progress < maxProgress) {
            progress += progressor.getProgress()
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

    protected abstract fun findRecipe(input: INPUT, level: ServerLevel): RECIPE?

    protected abstract fun canComplete(recipe: RECIPE, input: INPUT, output: OUTPUT): Boolean

    protected abstract fun onComplete(recipe: RECIPE, input: INPUT, output: OUTPUT)

    //    Progressor    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    interface Progressor<INPUT : Any, RECIPE : Any> : HTRecipeHandler.Progressor {
        fun getMaxProgress(recipe: RECIPE, input: INPUT): Int
    }

    //    Cached    //

    /**
     * [HTSingleRecipeCache]に基づいてレシピを取得する[HTSingleRecipeHandler]の拡張クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    abstract class Cached<INPUT : Any, OUTPUT : Any, RECIPE>(val cache: HTSingleRecipeCache<INPUT, RECIPE>) :
        HTSingleRecipeHandler<INPUT, OUTPUT, RECIPE>()
        where RECIPE : Predicate<INPUT>, RECIPE : Function<INPUT, OUTPUT> {
        final override fun findRecipe(input: INPUT, level: ServerLevel): RECIPE? = cache.findFirstRecipe(input, level)

        override fun serialize(output: ValueOutput) {
            super.serialize(output)
            output.putChild(HTConstants.LAST_RECIPE, cache)
        }

        override fun deserialize(input: ValueInput) {
            super.deserialize(input)
            input.readChild(HTConstants.LAST_RECIPE, cache)
        }
    }
}
