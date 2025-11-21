package hiiragi283.ragium.api.serialization.codec

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.api.util.toIor
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

object MapBiCodecs {
    @JvmStatic
    fun <B : ByteBuf, F : Any, S : Any> pair(left: MapBiCodec<in B, F>, right: MapBiCodec<in B, S>): MapBiCodec<B, Pair<F, S>> =
        MapBiCodec.of(
            Codec.mapPair(left.codec, right.codec),
            StreamCodec.composite(
                left.streamCodec,
                Pair<F, S>::getFirst,
                right.streamCodec,
                Pair<F, S>::getSecond,
                ::Pair,
            ),
        )

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
                { Pair.of(Optional.of(it), Optional.empty()) },
                { Pair.of(Optional.empty(), Optional.of(it)) },
                { leftIn: L, rightIn: R -> Pair.of(Optional.of(leftIn), Optional.of(rightIn)) },
            )
        },
    )
}
