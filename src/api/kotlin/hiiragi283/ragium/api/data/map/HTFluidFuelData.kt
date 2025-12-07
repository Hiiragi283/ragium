package hiiragi283.ragium.api.data.map

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import io.netty.buffer.ByteBuf

@JvmInline
value class HTFluidFuelData(val time: Int) {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTFluidFuelData> = BiCodec.composite(
            BiCodecs.POSITIVE_INT.fieldOf("time").forGetter(HTFluidFuelData::time),
            ::HTFluidFuelData,
        )
    }
}
