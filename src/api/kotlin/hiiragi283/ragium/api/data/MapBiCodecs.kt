package hiiragi283.ragium.api.data

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs

object MapBiCodecs {
    @JvmStatic
    fun <B : ByteBuf, F : Any, S : Any> either(first: MapBiCodec<in B, F>, second: MapBiCodec<in B, S>): MapBiCodec<B, Either<F, S>> =
        MapBiCodec.of(
            Codec.mapEither(first.codec, second.codec),
            ByteBufCodecs.either(first.streamCodec, second.streamCodec),
        )
}
