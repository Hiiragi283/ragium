package hiiragi283.ragium.api.data

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ExtraCodecs
import java.util.*
import java.util.function.Function
import kotlin.collections.List

@ConsistentCopyVisibility
data class BiCodec<B : ByteBuf, V : Any> private constructor(val codec: Codec<V>, val streamCodec: StreamCodec<B, V>) {
    companion object {
        @JvmStatic
        fun <B : ByteBuf, V : Any> of(codec: Codec<V>, streamCodec: StreamCodec<B, V>): BiCodec<B, V> = BiCodec(codec, streamCodec)

        @JvmField
        val BOOL: BiCodec<ByteBuf, Boolean> = of(Codec.BOOL, ByteBufCodecs.BOOL)

        @JvmField
        val SHORT: BiCodec<ByteBuf, Short> = of(Codec.SHORT, ByteBufCodecs.SHORT)

        @JvmField
        val INT: BiCodec<ByteBuf, Int> = of(Codec.INT, ByteBufCodecs.VAR_INT)

        @JvmField
        val LONG: BiCodec<ByteBuf, Long> = of(Codec.LONG, ByteBufCodecs.VAR_LONG)

        @JvmField
        val FLOAT: BiCodec<ByteBuf, Float> = of(Codec.FLOAT, ByteBufCodecs.FLOAT)

        @JvmField
        val DOUBLE: BiCodec<ByteBuf, Double> = of(Codec.DOUBLE, ByteBufCodecs.DOUBLE)

        @JvmField
        val STRING: BiCodec<ByteBuf, String> = of(Codec.STRING, ByteBufCodecs.STRING_UTF8)

        @JvmStatic
        fun intRange(min: Int, max: Int): BiCodec<ByteBuf, Int> = of(Codec.intRange(min, max), ByteBufCodecs.VAR_INT)

        @JvmStatic
        fun floatRange(min: Float, max: Float): BiCodec<ByteBuf, Float> = of(Codec.floatRange(min, max), ByteBufCodecs.FLOAT)

        @JvmStatic
        fun doubleRange(min: Double, max: Double): BiCodec<ByteBuf, Double> = of(Codec.doubleRange(min, max), ByteBufCodecs.DOUBLE)

        @JvmStatic
        fun <B : ByteBuf, C : Any, T1 : Any> composite(codec: MapBiCodec<in B, T1>, getter: (C) -> T1, factory: (T1) -> C): BiCodec<B, C> =
            of(
                RecordCodecBuilder.create { instance ->
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
        ): BiCodec<B, C> = of(
            RecordCodecBuilder.create { instance ->
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
        ): BiCodec<B, C> = of(
            RecordCodecBuilder.create { instance ->
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
        ): BiCodec<B, C> = of(
            RecordCodecBuilder.create { instance ->
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
    }

    // Encode & Decode
    fun <T : Any> encode(ops: DynamicOps<T>, input: V): DataResult<T> = codec.encodeStart(ops, input)

    fun <T : Any> decode(ops: DynamicOps<T>, input: T): DataResult<V> = codec.parse(ops, input)

    fun encode(buf: B, input: V) {
        streamCodec.encode(buf, input)
    }

    fun decode(buf: B): V = streamCodec.decode(buf)

    // Convert
    fun <S : Any> xmap(to: (V) -> S, from: (S) -> V): BiCodec<B, S> = of(codec.xmap(to, from), streamCodec.map(to, from))

    fun <S : Any> flatXmap(to: (V) -> DataResult<S>, from: (S) -> DataResult<V>): BiCodec<B, S> = of(
        codec.flatXmap(to, from),
        streamCodec.map(
            { value: V -> to(value).orThrow },
            { value: S -> from(value).orThrow },
        ),
    )

    fun validate(validator: (V) -> DataResult<V>): BiCodec<B, V> = flatXmap(validator, validator)

    fun toMap(): MapBiCodec<B, V> = MapBiCodec.of(MapCodec.assumeMapUnsafe(codec), streamCodec)

    fun <S : B> cast(): BiCodec<S, V> = of(codec, streamCodec.cast())

    // MapBiCodec
    fun fieldOf(name: String): MapBiCodec<B, V> = MapBiCodec.of(codec.fieldOf(name), streamCodec)

    fun optionalFieldOf(name: String): MapBiCodec<B, Optional<V>> = MapBiCodec.of(codec.optionalFieldOf(name), streamCodec.toOptional())

    fun optionalFieldOf(name: String, defaultValue: V): MapBiCodec<B, V> =
        MapBiCodec.of(codec.optionalFieldOf(name, defaultValue), streamCodec)

    fun optionalOrElseField(name: String, defaultValue: V): MapBiCodec<B, V> =
        MapBiCodec.of(codec.fieldOf(name).orElse(defaultValue), streamCodec)

    // List
    fun listOf(): BiCodec<B, List<V>> = of(codec.listOf(), streamCodec.listOf())

    fun listOf(min: Int, max: Int): BiCodec<B, List<V>> = of(codec.listOf(min, max), streamCodec.listOf())

    fun listOf(range: IntRange): BiCodec<B, List<V>> = listOf(range.first, range.last)

    fun listOrElement(): BiCodec<B, List<V>> = of(codec.listOrElement(), streamCodec.listOf())

    fun listOrElement(min: Int, max: Int): BiCodec<B, List<V>> = of(codec.listOrElement(min, max), streamCodec.listOf())

    // Optional
    fun toOptional(): BiCodec<B, Optional<V>> = of(ExtraCodecs.optionalEmptyMap(codec), streamCodec.toOptional())
}

private fun <A : Any> Codec<A>.listOrElement(): Codec<List<A>> = Codec.either(this.listOf(), this).xmap(
    { either: Either<List<A>, A> -> either.map(Function.identity(), ::listOf) },
    { list: List<A> -> if (list.size == 1) Either.right(list[0]) else Either.left(list) },
)

private fun <A : Any> Codec<A>.listOrElement(min: Int, max: Int): Codec<List<A>> = Codec.either(this.listOf(min, max), this).xmap(
    { either: Either<List<A>, A> -> either.map(Function.identity(), ::listOf) },
    { list: List<A> -> if (list.size == 1) Either.right(list[0]) else Either.left(list) },
)

/**
 * [List]の[StreamCodec]に変換します。
 */
private fun <B : ByteBuf, V : Any> StreamCodec<B, V>.listOf(): StreamCodec<B, List<V>> = apply(ByteBufCodecs.list())

/**
 * [Optional]の[StreamCodec]に変換します。
 */
private fun <B : ByteBuf, V : Any> StreamCodec<B, V>.toOptional(): StreamCodec<B, Optional<V>> = ByteBufCodecs.optional(this)
