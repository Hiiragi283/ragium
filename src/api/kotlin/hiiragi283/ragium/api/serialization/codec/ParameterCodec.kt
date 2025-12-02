package hiiragi283.ragium.api.serialization.codec

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import java.util.function.Function

@ConsistentCopyVisibility
@JvmRecord
data class ParameterCodec<B : ByteBuf, C : Any, V : Any> private constructor(
    val codec: MapCodec<V>,
    val streamCodec: StreamCodec<B, V>,
    val getter: Function<C, V>,
) {
    companion object {
        @JvmStatic
        fun <B : ByteBuf, C : Any, V : Any> of(
            codec: MapCodec<V>,
            streamCodec: StreamCodec<B, V>,
            getter: Function<C, V>,
        ): ParameterCodec<B, C, V> = ParameterCodec(codec, streamCodec, getter)

        @JvmStatic
        fun <B : ByteBuf, C : Any, V : Any> of(codec: MapBiCodec<B, V>, getter: Function<C, V>): ParameterCodec<B, C, V> =
            of(codec.codec, codec.streamCodec, getter)
    }

    fun toRecordParam(): RecordCodecBuilder<C, V> = codec.forGetter(getter)
}
