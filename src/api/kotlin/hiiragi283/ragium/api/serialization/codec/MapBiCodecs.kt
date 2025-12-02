package hiiragi283.ragium.api.serialization.codec

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.api.util.toIor
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

object MapBiCodecs {
    @JvmStatic
    fun <B : ByteBuf, F : Any, S : Any> either(first: MapBiCodec<in B, F>, second: MapBiCodec<in B, S>): MapBiCodec<B, Either<F, S>> =
        MapBiCodec.of(
            Codec.mapEither(first.codec, second.codec),
            ByteBufCodecs.either(first.streamCodec, second.streamCodec),
        )

    @JvmStatic
    fun <B : ByteBuf, F : Any, S : Any> pair(left: MapBiCodec<in B, F>, right: MapBiCodec<in B, S>): MapBiCodec<B, Pair<F, S>> =
        MapBiCodec.composite(left.forGetter(Pair<F, S>::first), right.forGetter(Pair<F, S>::second), ::Pair)

    @JvmStatic
    fun <B : ByteBuf, L : Any, R : Any> ior(
        left: MapBiCodec<in B, Optional<L>>,
        right: MapBiCodec<in B, Optional<R>>,
    ): MapBiCodec<B, Ior<L, R>> = pair(left, right).flatXmap(
        { pair: Pair<Optional<L>, Optional<R>> ->
            (pair.first.getOrNull() to pair.second.getOrNull()).toIor() ?: error("Cannot serialize empty ior")
        },
        { ior: Ior<L, R> ->
            ior.fold(
                { Optional.of(it) to Optional.empty() },
                { Optional.empty<L>() to Optional.of(it) },
                { leftIn: L, rightIn: R -> Optional.of(leftIn) to Optional.of(rightIn) },
            )
        },
    )
}
