package hiiragi283.ragium.api.data.map

import hiiragi283.core.api.math.toFraction
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import io.netty.buffer.ByteBuf
import org.apache.commons.lang3.math.Fraction

@JvmInline
value class HTFluidFertilizerData(val multiplier: Fraction) {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTFluidFertilizerData> = BiCodecs.POSITIVE_FRACTION.xmap(
            ::HTFluidFertilizerData,
            HTFluidFertilizerData::multiplier,
        )
    }

    constructor(multiplier: Float) : this(multiplier.toFraction())
}
