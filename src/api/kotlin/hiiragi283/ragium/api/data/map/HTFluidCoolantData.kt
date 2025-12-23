package hiiragi283.ragium.api.data.map

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import io.netty.buffer.ByteBuf

@JvmInline
value class HTFluidCoolantData(val amount: Int) {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTFluidCoolantData> = BiCodec.composite(
            BiCodecs.POSITIVE_INT.fieldOf("amount").forGetter(HTFluidCoolantData::amount),
            ::HTFluidCoolantData,
        )
    }
}
