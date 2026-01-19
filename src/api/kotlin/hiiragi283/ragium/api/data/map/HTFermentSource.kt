package hiiragi283.ragium.api.data.map

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import io.netty.buffer.ByteBuf

@JvmInline
value class HTFermentSource(val level: Int) {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTFermentSource> = BiCodec.composite(
            BiCodecs.POSITIVE_INT.fieldOf("level").forGetter(HTFermentSource::level),
            ::HTFermentSource,
        )
    }
}
