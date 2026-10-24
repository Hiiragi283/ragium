package hiiragi283.lib.recipe.base

import com.mojang.serialization.codecs.RecordCodecBuilder
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

    //    Single    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    interface Single<INPUT : RecipeInput, INPUT_A : Any> : HTProgressRecipe<INPUT> {
        fun getProgressData(input: INPUT_A): HTProgressData
    }

    //    Double    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    interface Double<INPUT : RecipeInput, INPUT_A : Any, INPUT_B : Any> : HTProgressRecipe<INPUT> {
        fun getProgressData(first: INPUT_A, second: INPUT_B): HTProgressData
    }

    //    Static    //

    /**
     * 一定の[HTProgressData]を提供するインターフェースです。
     * @author Hiiragi Tsubasa
     * @since 26.1.11
     */
    interface Holder {
        companion object {
            /**
             * @since 26.1.12
             */
            @JvmStatic
            fun <T : Holder> progressCodec(): RecordCodecBuilder<T, HTProgressData> = HTProgressData.CODEC.forGetter(Holder::progressData)
        }

        val progressData: HTProgressData
    }
}
