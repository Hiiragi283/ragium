package hiiragi283.ragium.api.data.map

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import io.netty.buffer.ByteBuf
import org.apache.commons.lang3.math.Fraction

@JvmRecord
data class HTRockGenerationData(val waterChance: Fraction, val lavaChance: Fraction) {
    companion object {
        @JvmStatic
        private val FRACTION_CODEC: BiCodec<ByteBuf, Fraction> = BiCodecs.fractionRange(Fraction.ZERO, Fraction.ONE)

        @JvmField
        val CODEC: BiCodec<ByteBuf, HTRockGenerationData> = BiCodec.composite(
            FRACTION_CODEC.fieldOf("water_chance").forGetter(HTRockGenerationData::waterChance),
            FRACTION_CODEC.fieldOf("lava_chance").forGetter(HTRockGenerationData::lavaChance),
            ::HTRockGenerationData,
        )
    }
}
