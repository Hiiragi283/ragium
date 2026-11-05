package hiiragi283.lib.recipe.handler

import hiiragi283.lib.recipe.handler.input.HTInputSlot
import hiiragi283.lib.recipe.handler.output.HTOutputSlot
import hiiragi283.lib.transfer.useTransaction
import net.minecraft.core.TypedInstance
import net.minecraft.server.level.ServerLevel
import net.neoforged.neoforge.transfer.transaction.Transaction
import java.util.function.Function

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
        val input: INPUT = inputCache ?: getInput()
        inputCache = input
        val recipe: RECIPE = findRecipe(input, level) ?: return run {
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

    //    Basic    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    open class Basic<INPUT : TypedInstance<*>, OUTPUT : Any, RECIPE : Function<INPUT, OUTPUT>>(
        private val inputHandler: HTInputSlot<INPUT>,
        private val outputHandler: HTOutputSlot<OUTPUT>,
        private val progressor1: Progressor<INPUT, RECIPE>,
        private val recipeFinder: (INPUT) -> RECIPE?,
        private val matchingStack: (RECIPE, INPUT) -> INPUT,
        private val onComplete: Runnable,
    ) : HTSingleRecipeHandler<INPUT, OUTPUT, RECIPE>() {
        override fun createProgressor(): Progressor<INPUT, RECIPE> = progressor1

        override fun getInput(): INPUT = inputHandler.getStoredInput()

        override fun findRecipe(input: INPUT, level: ServerLevel): RECIPE? = recipeFinder(input)

        protected fun getMatchingStack(recipe: RECIPE, input: INPUT): INPUT = matchingStack(recipe, input)

        override fun canComplete(recipe: RECIPE, input: INPUT, output: OUTPUT): Boolean = useTransaction { transaction: Transaction ->
            when {
                inputHandler.use(getMatchingStack(recipe, input), transaction).failed -> false
                else -> outputHandler.take(output, transaction) == HTOutputSlot.TakeResult.FULL
            }
        }

        override fun onComplete(recipe: RECIPE, input: INPUT, output: OUTPUT) {
            useTransaction { transaction: Transaction ->
                inputHandler.use(getMatchingStack(recipe, input), transaction)
                outputHandler.take(output, transaction)
                transaction.commit()
            }
            onComplete.run()
        }
    }
}
