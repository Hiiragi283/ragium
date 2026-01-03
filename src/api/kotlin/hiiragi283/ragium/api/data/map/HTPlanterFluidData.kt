package hiiragi283.ragium.api.data.map

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import io.netty.buffer.ByteBuf
import org.apache.commons.lang3.math.Fraction

@JvmInline
value class HTPlanterFluidData(val multiplier: Fraction) {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTPlanterFluidData> = BiCodecs.NON_NEGATIVE_FRACTION.xmap(
            ::HTPlanterFluidData,
            HTPlanterFluidData::multiplier,
        )
    }
}
