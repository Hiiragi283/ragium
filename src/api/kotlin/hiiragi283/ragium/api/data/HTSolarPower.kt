package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import io.netty.buffer.ByteBuf

data class HTSolarPower(val multiplier: Float) {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTSolarPower> = BiCodec.composite(
            BiCodecs.POSITIVE_FLOAT.fieldOf("multiplier"),
            HTSolarPower::multiplier,
            ::HTSolarPower,
        )
    }
}
