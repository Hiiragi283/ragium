package hiiragi283.lib.recipe.base

import net.minecraft.world.item.crafting.RecipeInput

/**
 * 処理時間または消費エネルギーを提供するインターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTProgressRecipe<INPUT : RecipeInput> {
    /**
     * 入力からレシピの処理時間または消費エネルギーを取得します。
     */
    fun getProgressData(input: INPUT): HTProgressData

    //    Simple    //

    /**
     * 一定の[HTProgressData]を提供する[HTProgressRecipe]の拡張インターフェースです。
     * @param INPUT レシピの入力となるクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    interface Simple<INPUT : RecipeInput> : HTProgressRecipe<INPUT> {
        val progressData: HTProgressData

        override fun getProgressData(input: INPUT): HTProgressData = progressData
    }
}
