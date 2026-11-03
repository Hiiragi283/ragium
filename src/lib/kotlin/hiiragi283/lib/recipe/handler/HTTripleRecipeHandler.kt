package hiiragi283.lib.recipe.handler

import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.handler.cache.HTTripleRecipeCache
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.common.util.TriPredicate
import org.apache.commons.lang3.function.TriFunction

/**
 * 3つの入力に対する[HTRecipeHandler]の拡張クラスです。
 * @param IN_A 1番目のレシピの入力となるクラス
 * @param IN_B 2番目のレシピの入力となるクラス
 * @param OUT レシピの出力となるクラス
 * @param RECIPE 提供するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
abstract class HTTripleRecipeHandler<IN_A : Any, IN_B : Any, IN_C : Any, OUT : Any, RECIPE : TriFunction<IN_A, IN_B, IN_C, OUT>> : HTRecipeHandler() {

    protected val progressor: Progressor<IN_A, IN_B, IN_C, RECIPE> = createProgressor()

    protected abstract fun createProgressor(): Progressor<IN_A, IN_B, IN_C, RECIPE>

    private var firstInputCache: IN_A? = null
    private var secondInputCache: IN_B? = null
    private var thirdInputCache: IN_C? = null
    private var outputCache: OUT? = null

    override fun run() {
        super.run()
        firstInputCache = null
        secondInputCache = null
        thirdInputCache = null
        outputCache = null
    }

    override fun tick(level: ServerLevel): Boolean {
        if (!shouldCheck) return false
        // インプットに一致するレシピを探索する
        val firstInput: IN_A = firstInputCache ?: getFirstInput()
        val secondInput: IN_B = secondInputCache ?: getSecondInput()
        val thirdInput: IN_C = thirdInputCache ?: getThirdInput()
        firstInputCache = firstInput
        secondInputCache = secondInput
        thirdInputCache = thirdInput
        val recipe: RECIPE = findRecipe(firstInput, secondInput, thirdInput, level) ?: return run {
            shouldCheck = false
            firstInputCache = null
            secondInputCache = null
            thirdInputCache = null
            updateProgress(-1)
            false
        }
        // アウトプットに完成品を搬出できるか判定する
        val output: OUT = outputCache ?: recipe.apply(firstInput, secondInput, thirdInput)
        outputCache = output
        if (!canProgress) {
            if (canComplete(recipe, firstInput, secondInput, thirdInput, output)) {
                canProgress = true
            } else {
                return false
            }
        }
        // レシピの最大進捗量を更新する
        val maxProgress: Int = progressor.getMaxProgress(recipe, firstInput, secondInput, thirdInput)
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
            onComplete(recipe, firstInput, secondInput, thirdInput, output)
            firstInputCache = null
            secondInputCache = null
            thirdInputCache = null
            outputCache = null
        }
        return true
    }

    protected abstract fun getFirstInput(): IN_A

    protected abstract fun getSecondInput(): IN_B

    protected abstract fun getThirdInput(): IN_C

    protected abstract fun findRecipe(first: IN_A, second: IN_B, third: IN_C, level: ServerLevel): RECIPE?

    protected abstract fun canComplete(recipe: RECIPE, first: IN_A, second: IN_B, third: IN_C, output: OUT): Boolean

    protected abstract fun onComplete(recipe: RECIPE, first: IN_A, second: IN_B, third: IN_C, output: OUT)

    //    Progressor    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.12
     */
    interface Progressor<IN_A : Any, IN_B : Any, IN_C : Any, RECIPE : Any> : HTRecipeHandler.Progressor {
        fun getMaxProgress(recipe: RECIPE, first: IN_A, second: IN_B, third: IN_C): Int
    }

    //    Cached    //

    /**
     * [HTTripleRecipeCache]に基づいてレシピを取得する[HTTripleRecipeHandler]の拡張クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.12
     */
    abstract class Cached<IN_A : Any, IN_B : Any, IN_C : Any, OUT : Any, RECIPE>(val cache: HTTripleRecipeCache<IN_A, IN_B, IN_C, RECIPE>) :
        HTTripleRecipeHandler<IN_A, IN_B, IN_C, OUT, RECIPE>()
        where RECIPE : TriPredicate<IN_A, IN_B, IN_C>, RECIPE : TriFunction<IN_A, IN_B, IN_C, OUT> {
        final override fun findRecipe(first: IN_A, second: IN_B, third: IN_C, level: ServerLevel): RECIPE? =
            cache.findFirstRecipe(first, second, third, level)

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
