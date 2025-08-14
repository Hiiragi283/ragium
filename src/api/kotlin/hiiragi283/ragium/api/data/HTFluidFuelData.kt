package hiiragi283.ragium.api.data

import io.netty.buffer.ByteBuf

data class HTFluidFuelData(val time: Int, val amount: Int) {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTFluidFuelData> = BiCodec.composite(
            BiCodecs.POSITIVE_INT.fieldOf("time"),
            HTFluidFuelData::time,
            BiCodecs.POSITIVE_INT.fieldOf("amount"),
            HTFluidFuelData::amount,
            ::HTFluidFuelData,
        )
    }
}
