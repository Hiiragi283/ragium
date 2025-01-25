package hiiragi283.ragium.api.util

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.stringCodec
import hiiragi283.ragium.api.extension.stringStreamCodec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable

enum class HTTemperatureType : StringRepresentable {
    HEATING,
    COOLING,
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTTemperatureType> = stringCodec(HTTemperatureType.entries)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTTemperatureType> = stringStreamCodec(HTTemperatureType.entries)
    }

    override fun getSerializedName(): String = name.lowercase()
}
