package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import io.netty.buffer.ByteBuf

@JvmRecord
data class HTFluidFuelData(val amount: Int) {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTFluidFuelData> = BiCodec.composite(
            BiCodecs.POSITIVE_INT.fieldOf("amount"),
            HTFluidFuelData::amount,
            ::HTFluidFuelData,
        )
    }
}
