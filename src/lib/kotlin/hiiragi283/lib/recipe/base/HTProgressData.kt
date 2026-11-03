package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.lib.text.HTHasText
import hiiragi283.lib.text.Text
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

/**
 * 処理時間を保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTProgressData(val time: Int) : HTHasText {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTProgressData> = HTCodecs.recordMap { instance ->
            instance.group(
                HTCodecs.POSITIVE_INT.fieldOf(HTConstants.TIME).forGetter(HTProgressData::time),
            ).apply(instance, ::HTProgressData)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTProgressData> = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            HTProgressData::time,
            ::HTProgressData,
        )

        @JvmStatic
        fun time(value: Int): HTProgressData = HTProgressData(value)
    }

    override fun getText(): Text = HTCommonTranslation.SECONDS.translate(time / 20, time)
}
