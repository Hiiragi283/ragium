package hiiragi283.ragium.api.data

import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec

/**
 * [MapCodec]と[StreamCodec]を束ねたデータクラス
 * @see [BiCodec]
 * @see [BiCodecs]
 */
@ConsistentCopyVisibility
data class MapBiCodec<B : ByteBuf, V : Any> private constructor(val codec: MapCodec<V>, val streamCodec: StreamCodec<B, V>) {
    companion object {
        @JvmStatic
        fun <B : ByteBuf, V : Any> of(codec: MapCodec<V>, streamCodec: StreamCodec<B, V>): MapBiCodec<B, V> = MapBiCodec(codec, streamCodec)

        @JvmStatic
        fun <B : ByteBuf, C : Any, T1 : Any> composite(
            codec: MapBiCodec<in B, T1>,
            getter: (C) -> T1,
            factory: (T1) -> C,
        ): MapBiCodec<B, C> = of(
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(codec.codec.forGetter(getter)).apply(instance, factory)
            },
            StreamCodec.composite(codec.streamCodec, getter, factory),
        )

        @JvmStatic
        fun <B : ByteBuf, C : Any, T1 : Any, T2 : Any> composite(
            codec1: MapBiCodec<in B, T1>,
            getter1: (C) -> T1,
            codec2: MapBiCodec<in B, T2>,
            getter2: (C) -> T2,
            factory: (T1, T2) -> C,
        ): MapBiCodec<B, C> = of(
            RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        codec1.codec.forGetter(getter1),
                        codec2.codec.forGetter(getter2),
                    ).apply(instance, factory)
            },
            StreamCodec.composite(codec1.streamCodec, getter1, codec2.streamCodec, getter2, factory),
        )

        @JvmStatic
        fun <B : ByteBuf, C : Any, T1 : Any, T2 : Any, T3 : Any> composite(
            codec1: MapBiCodec<in B, T1>,
            getter1: (C) -> T1,
            codec2: MapBiCodec<in B, T2>,
            getter2: (C) -> T2,
            codec3: MapBiCodec<in B, T3>,
            getter3: (C) -> T3,
            factory: (T1, T2, T3) -> C,
        ): MapBiCodec<B, C> = of(
            RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        codec1.codec.forGetter(getter1),
                        codec2.codec.forGetter(getter2),
                        codec3.codec.forGetter(getter3),
                    ).apply(instance, factory)
            },
            StreamCodec.composite(
                codec1.streamCodec,
                getter1,
                codec2.streamCodec,
                getter2,
                codec3.streamCodec,
                getter3,
                factory,
            ),
        )

        @JvmStatic
        fun <B : ByteBuf, C : Any, T1 : Any, T2 : Any, T3 : Any, T4 : Any> composite(
            codec1: MapBiCodec<in B, T1>,
            getter1: (C) -> T1,
            codec2: MapBiCodec<in B, T2>,
            getter2: (C) -> T2,
            codec3: MapBiCodec<in B, T3>,
            getter3: (C) -> T3,
            codec4: MapBiCodec<in B, T4>,
            getter4: (C) -> T4,
            factory: (T1, T2, T3, T4) -> C,
        ): MapBiCodec<B, C> = of(
            RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        codec1.codec.forGetter(getter1),
                        codec2.codec.forGetter(getter2),
                        codec3.codec.forGetter(getter3),
                        codec4.codec.forGetter(getter4),
                    ).apply(instance, factory)
            },
            StreamCodec.composite(
                codec1.streamCodec,
                getter1,
                codec2.streamCodec,
                getter2,
                codec3.streamCodec,
                getter3,
                codec4.streamCodec,
                getter4,
                factory,
            ),
        )

        @JvmStatic
        fun <B : ByteBuf, V : Any> unit(instance: V): MapBiCodec<B, V> = of(MapCodec.unit(instance), StreamCodec.unit(instance))
    }

    /**
     * 指定された[to]と[from]に基づいて，別の[MapBiCodec]に変換します。
     * @param S 変換後のコーデックの対象となるクラス
     * @param to [V]から[S]に変換するブロック
     * @param from [S]から[V]に変換するブロック
     * @return [S]を対象とする[MapBiCodec]
     */
    fun <S : Any> xmap(to: (V) -> S, from: (S) -> V): MapBiCodec<B, S> = of(codec.xmap(to, from), streamCodec.map(to, from))

    /**
     * 指定された[to]と[from]に基づいて，別の[MapBiCodec]に変換します。
     * @param S 変換後のコーデックの対象となるクラス
     * @param to [V]から[S]の[DataResult]に変換するブロック
     * @param from [S]から[V]の[DataResult]にに変換するブロック
     * @return [S]を対象とする[MapBiCodec]
     */
    fun <S : Any> flatXmap(to: (V) -> DataResult<S>, from: (S) -> DataResult<V>): MapBiCodec<B, S> = of(
        codec.flatXmap(to, from),
        streamCodec.map(
            { value: V -> to(value).orThrow },
            { value: S -> from(value).orThrow },
        ),
    )

    fun validate(validator: (V) -> DataResult<V>): MapBiCodec<B, V> = flatXmap(validator, validator)
}
