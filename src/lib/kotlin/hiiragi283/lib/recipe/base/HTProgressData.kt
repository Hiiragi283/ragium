package hiiragi283.lib.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.convert
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.lib.text.HTHasText
import hiiragi283.lib.text.Text
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.unwrap

/**
 * 処理時間または消費エネルギーを保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
sealed interface HTProgressData : HTHasText {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTProgressData> = Codec
            .mapEither(Time.CODEC, Energy.CODEC)
            .convert()
            .xmap(Either<Time, Energy>::unwrap) { progressData: HTProgressData ->
                when (progressData) {
                    is Energy -> Either.Right(progressData)
                    is Time -> Either.Left(progressData)
                }
            }

        /**
         * 処理時間を保持する，新しい[HTProgressData]のインスタンスを作成します。
         */
        @JvmStatic
        fun time(value: Int): HTProgressData = Time(value)

        /**
         * 消費エネルギーを保持する，新しい[HTProgressData]のインスタンスを作成します。
         */
        @JvmStatic
        fun energy(value: Int): HTProgressData = Energy(value)
    }

    /**
     * 処理全体で消費するエネルギーを取得します。
     * @param energyRate 1 tickあたりのエネルギーの消費率
     */
    fun getTotalEnergy(energyRate: Int): Int

    /**
     * 処理全体にかかる時間を取得します。
     * @param energyRate 1 tickあたりのエネルギーの消費率
     */
    fun getProcessTime(energyRate: Int): Int

    /**
     * 処理時間を保持する[HTProgressData]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    @JvmInline
    value class Time(val value: Int) : HTProgressData {
        companion object {
            @JvmField
            val CODEC: MapCodec<Time> = HTCodecs.NON_NEGATIVE_INT.fieldOf(HTConstants.TIME).xmap(::Time, Time::value)
        }

        init {
            require(value >= 0) { "Time must be non-negative" }
        }

        override fun getTotalEnergy(energyRate: Int): Int = value * energyRate

        override fun getProcessTime(energyRate: Int): Int = value

        override fun getText(): Text = HTCommonTranslation.SECONDS.translate(value, value / 20)
    }

    /**
     * 消費エネルギーを保持する[HTProgressData]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    @JvmInline
    value class Energy(val value: Int) : HTProgressData {
        companion object {
            @JvmField
            val CODEC: MapCodec<Energy> = HTCodecs.NON_NEGATIVE_INT.fieldOf(HTConstants.ENERGY).xmap(::Energy, Energy::value)
        }

        init {
            require(value > 0) { "Energy must be positive" }
        }

        override fun getTotalEnergy(energyRate: Int): Int = value

        override fun getProcessTime(energyRate: Int): Int = value / energyRate

        override fun getText(): Text = HTCommonTranslation.STORED_FE.translate(value)
    }
}
