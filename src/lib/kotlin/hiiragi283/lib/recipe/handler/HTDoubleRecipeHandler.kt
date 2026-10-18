package hiiragi283.lib.recipe.handler

import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.handler.cache.HTDoubleRecipeCache
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import java.util.function.BiFunction
import java.util.function.BiPredicate

/**
 * 2つの入力に対する[HTRecipeHandler]の拡張クラスです。
 * @param INPUT_A 1番目のレシピの入力となるクラス
 * @param INPUT_B 2番目のレシピの入力となるクラス
 * @param OUTPUT レシピの出力となるクラス
 * @param RECIPE 提供するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
abstract class HTDoubleRecipeHandler<
    INPUT_A : Any,
    INPUT_B : Any,
    OUTPUT : Any,
    RECIPE : BiFunction<INPUT_A, INPUT_B, OUTPUT>
    > : HTRecipeHandler() {

    protected val progressor: Progressor<INPUT_A, INPUT_B, RECIPE> = createProgressor()

    protected abstract fun createProgressor(): Progressor<INPUT_A, INPUT_B, RECIPE>

    private var firstInputCache: INPUT_A? = null
    private var secondInputCache: INPUT_B? = null
    private var outputCache: OUTPUT? = null

    override fun run() {
        super.run()
        firstInputCache = null
        secondInputCache = null
        outputCache = null
    }

    override fun tick(level: ServerLevel): Boolean {
        if (!shouldCheck) return false
        // インプットに一致するレシピを探索する
        val firstInput: INPUT_A = getFirstInput()
        val secondInput: INPUT_B = getSecondInput()
        firstInputCache = firstInput
        secondInputCache = secondInput
        val recipe: RECIPE = findRecipe(firstInput, secondInput, level) ?: return run {
            shouldCheck = false
            firstInputCache = null
            secondInputCache = null
            updateProgress(-1)
            false
        }
        // アウトプットに完成品を搬出できるか判定する
        val output: OUTPUT = recipe.apply(firstInput, secondInput)
        outputCache = output
        if (!canProgress) {
            if (canComplete(recipe, firstInput, secondInput, output)) {
                canProgress = true
            } else {
                return false
            }
        }
        // レシピの最大進捗量を更新する
        val maxProgress: Int = progressor.getMaxProgress(recipe, firstInput, secondInput)
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
            onComplete(recipe, firstInput, secondInput, output)
            firstInputCache = null
            secondInputCache = null
            outputCache = null
        }
        return true
    }

    protected abstract fun getFirstInput(): INPUT_A

    protected abstract fun getSecondInput(): INPUT_B

    protected abstract fun findRecipe(first: INPUT_A, second: INPUT_B, level: ServerLevel): RECIPE?

    protected abstract fun canComplete(recipe: RECIPE, first: INPUT_A, second: INPUT_B, output: OUTPUT): Boolean

    protected abstract fun onComplete(recipe: RECIPE, first: INPUT_A, second: INPUT_B, output: OUTPUT)

    //    Progressor    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    interface Progressor<INPUT_A : Any, INPUT_B : Any, RECIPE : Any> {
        fun getMaxProgress(recipe: RECIPE, first: INPUT_A, second: INPUT_B): Int

        fun getProgress(): Int
    }

    //    Cached    //

    /**
     * [HTDoubleRecipeCache]に基づいてレシピを取得する[HTDoubleRecipeHandler]の拡張クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    abstract class Cached<
        INPUT_A : Any,
        INPUT_B : Any,
        OUTPUT : Any,
        RECIPE
        >(
        val cache: HTDoubleRecipeCache<INPUT_A, INPUT_B, RECIPE>
    ) : HTDoubleRecipeHandler<INPUT_A, INPUT_B, OUTPUT, RECIPE>()
        where RECIPE : BiPredicate<INPUT_A, INPUT_B>, RECIPE : BiFunction<INPUT_A, INPUT_B, OUTPUT> {
        final override fun findRecipe(first: INPUT_A, second: INPUT_B, level: ServerLevel): RECIPE? =
            cache.findFirstRecipe(first, second, level)

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
