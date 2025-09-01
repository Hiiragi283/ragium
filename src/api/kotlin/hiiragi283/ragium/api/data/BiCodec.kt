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

/**
 * [Codec]と[StreamCodec]を束ねたデータクラス
 * @see [BiCodecs]
 * @see [MapBiCodec]
 */
@ConsistentCopyVisibility
data class BiCodec<B : ByteBuf, V : Any> private constructor(val codec: Codec<V>, val streamCodec: StreamCodec<B, V>) {
    companion object {
        /**
         * 指定された[codec]と[streamCodec]から[BiCodec]を返します。
         * @param B [StreamCodec]で使われるパケットのクラス
         * @param V コーデック対象のクラス
         */
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

        /**
         * 指定された[min]と[max]から[BiCodec]を返します。
         * @param min 範囲の最小値
         * @param max 範囲の最大値
         * @return [min]と[max]を含む範囲の値のみを通す[BiCodec]
         */
        @JvmStatic
        fun intRange(min: Int, max: Int): BiCodec<ByteBuf, Int> = of(Codec.intRange(min, max), ByteBufCodecs.VAR_INT)

        /**
         * 指定された[min]と[max]から[BiCodec]を返します。
         * @param min 範囲の最小値
         * @param max 範囲の最大値
         * @return [min]と[max]を含む範囲の値のみを通す[BiCodec]
         */
        @JvmStatic
        fun floatRange(min: Float, max: Float): BiCodec<ByteBuf, Float> = of(Codec.floatRange(min, max), ByteBufCodecs.FLOAT)

        /**
         * 指定された[min]と[max]から[BiCodec]を返します。
         * @param min 範囲の最小値
         * @param max 範囲の最大値
         * @return [min]と[max]を含む範囲の値のみを通す[BiCodec]
         */
        @JvmStatic
        fun doubleRange(min: Double, max: Double): BiCodec<ByteBuf, Double> = of(Codec.doubleRange(min, max), ByteBufCodecs.DOUBLE)

        /**
         * 指定された[codec]に基づいて，別の[BiCodec]を生成します。
         * @param T1 [factory]の第1引数に使われるクラス
         * @param C 変換後のコーデックの対象となるクラス
         */
        @JvmStatic
        fun <B : ByteBuf, C : Any, T1 : Any> composite(codec: MapBiCodec<in B, T1>, getter: (C) -> T1, factory: (T1) -> C): BiCodec<B, C> =
            of(
                RecordCodecBuilder.create { instance ->
                    instance.group(codec.codec.forGetter(getter)).apply(instance, factory)
                },
                StreamCodec.composite(codec.streamCodec, getter, factory),
            )

        /**
         * 指定された[codec1], [codec2]に基づいて，別の[BiCodec]を生成します。
         * @param T1 [factory]の第1引数に使われるクラス
         * @param T2 [factory]の第2引数に使われるクラス
         * @param C 変換後のコーデックの対象となるクラス
         */
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

        /**
         * 指定された[codec1], [codec2], [codec3]に基づいて，別の[BiCodec]を生成します。
         * @param T1 [factory]の第1引数に使われるクラス
         * @param T2 [factory]の第2引数に使われるクラス
         * @param T3 [factory]の第3引数に使われるクラス
         * @param C 変換後のコーデックの対象となるクラス
         */
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

        /**
         * 指定された[codec1], [codec2], [codec3], [codec4]に基づいて，別の[BiCodec]を生成します。
         * @param T1 [factory]の第1引数に使われるクラス
         * @param T2 [factory]の第2引数に使われるクラス
         * @param T3 [factory]の第3引数に使われるクラス
         * @param T4 [factory]の第4引数に使われるクラス
         * @param C 変換後のコーデックの対象となるクラス
         */
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

        /**
         * 指定された[instance]を常に返す[BiCodec]を返します。
         */
        @JvmStatic
        fun <B : ByteBuf, V : Any> unit(instance: V): BiCodec<B, V> = of(Codec.unit(instance), StreamCodec.unit(instance))

        /**
         * 指定された[codec]を，別の[BiCodec]に変換します。
         * @param X 変換後のコーデックの対象となるクラス
         * @param V [X]を継承したクラス
         * @return [X]を対象とした[BiCodec]
         */
        @JvmStatic
        inline fun <B : ByteBuf, reified X : Any, reified V : X> downCast(codec: BiCodec<B, V>): BiCodec<B, X> =
            codec.flatXmap(DataResult<X>::success) { value: X ->
                if (value is V) {
                    return@flatXmap DataResult.success(value)
                } else {
                    return@flatXmap DataResult.error { "Value $value cannot cast to ${X::class.java.canonicalName}" }
                }
            }
    }

    // Encode & Decode
    fun <T : Any> encode(ops: DynamicOps<T>, input: V): DataResult<T> = codec.encodeStart(ops, input)

    fun <T : Any> decode(ops: DynamicOps<T>, input: T): DataResult<V> = codec.parse(ops, input)

    fun encode(buf: B, input: V) {
        streamCodec.encode(buf, input)
    }

    fun decode(buf: B): V = streamCodec.decode(buf)

    // Convert

    /**
     * 指定された[to]と[from]に基づいて，別の[BiCodec]に変換します。
     * @param S 変換後のコーデックの対象となるクラス
     * @param to [V]から[S]に変換するブロック
     * @param from [S]から[V]に変換するブロック
     * @return [S]を対象とする[BiCodec]
     */
    fun <S : Any> xmap(to: (V) -> S, from: (S) -> V): BiCodec<B, S> = of(codec.xmap(to, from), streamCodec.map(to, from))

    /**
     * 指定された[to]と[from]に基づいて，別の[BiCodec]に変換します。
     * @param S 変換後のコーデックの対象となるクラス
     * @param to [V]から[S]の[DataResult]に変換するブロック
     * @param from [S]から[V]の[DataResult]にに変換するブロック
     * @return [S]を対象とする[BiCodec]
     */
    fun <S : Any> flatXmap(to: (V) -> DataResult<S>, from: (S) -> DataResult<V>): BiCodec<B, S> = of(
        codec.flatXmap(to, from),
        streamCodec.map(
            { value: V -> to(value).orThrow },
            { value: S -> from(value).orThrow },
        ),
    )

    fun validate(validator: (V) -> DataResult<V>): BiCodec<B, V> = flatXmap(validator, validator)

    fun <E : Any> dispatch(
        typeKey: String,
        type: (E) -> V,
        codec: (V) -> MapCodec<out E>,
        streamCodec: (V) -> StreamCodec<in B, out E>,
    ): BiCodec<B, E> = of(
        this.codec.dispatch(typeKey, type, codec),
        this.streamCodec.dispatch(type, streamCodec),
    )

    fun <E : Any> dispatch(type: (E) -> V, codec: (V) -> MapCodec<out E>, streamCodec: (V) -> StreamCodec<in B, out E>): BiCodec<B, E> =
        dispatch("type", type, codec, streamCodec)

    /**
     * 現在の[BiCodec]を[MapBiCodec]に変換します。
     */
    fun toMap(): MapBiCodec<B, V> = MapBiCodec.of(MapCodec.assumeMapUnsafe(codec), streamCodec)

    /**
     * [B]を[S]に置換した[BiCodec]を返します。
     * @param S [B]を継承したクラス
     */
    fun <S : B> cast(): BiCodec<S, V> = of(codec, streamCodec.cast())

    // MapBiCodec
    fun fieldOf(name: String): MapBiCodec<B, V> = MapBiCodec.of(codec.fieldOf(name), streamCodec)

    fun optionalFieldOf(name: String): MapBiCodec<B, Optional<V>> = MapBiCodec.of(codec.optionalFieldOf(name), streamCodec.toOptional())

    fun optionalFieldOf(name: String, defaultValue: V): MapBiCodec<B, V> =
        MapBiCodec.of(codec.optionalFieldOf(name, defaultValue), streamCodec)

    fun optionalFieldOf(name: String, defaultValue: () -> V): MapBiCodec<B, V> = optionalFieldOf(name).xmap(
        { optional: Optional<V> -> optional.orElseGet(defaultValue) },
        { value: V -> if (value == defaultValue()) Optional.empty() else Optional.of(value) },
    )

    fun optionalOrElseField(name: String, defaultValue: V): MapBiCodec<B, V> =
        MapBiCodec.of(codec.fieldOf(name).orElse(defaultValue), streamCodec)

    // List

    /**
     * 現在の[BiCodec]を[List]の[BiCodec]に変換します。
     */
    fun listOf(): BiCodec<B, List<V>> = of(codec.listOf(), streamCodec.listOf())

    /**
     * 現在の[BiCodec]を[List]の[BiCodec]に変換します。
     * @param min [List.size]の最小値
     * @param max [List.size]の最大値
     * @return [List.size]が制限された[List]の[BiCodec]
     */
    fun listOf(min: Int, max: Int): BiCodec<B, List<V>> = of(codec.listOf(min, max), streamCodec.listOf())

    /**
     * 現在の[BiCodec]を[List]の[BiCodec]に変換します。
     * @param range [List.size]の範囲
     * @return [List.size]が制限された[List]の[BiCodec]
     */
    fun listOf(range: IntRange): BiCodec<B, List<V>> = listOf(range.first, range.last)

    /**
     * 現在の[BiCodec]を，要素が一つの場合はそのままコーデックする[List]の[BiCodec]を返します。
     * @return [List]の[BiCodec]
     */
    fun listOrElement(): BiCodec<B, List<V>> = of(codec.listOrElement(), streamCodec.listOf())

    /**
     * 現在の[BiCodec]を，要素が一つの場合はそのままコーデックする[List]の[BiCodec]を返します。
     * @param min [List.size]の最小値
     * @param max [List.size]の最大値
     * @return [List.size]が制限された[List]の[BiCodec]
     */
    fun listOrElement(min: Int, max: Int): BiCodec<B, List<V>> = of(codec.listOrElement(min, max), streamCodec.listOf())

    // Optional

    /**
     * 現在の[BiCodec]を[Optional]の[BiCodec]に変換します。
     */
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

private fun <B : ByteBuf, V : Any> StreamCodec<B, V>.listOf(): StreamCodec<B, List<V>> = apply(ByteBufCodecs.list())

private fun <B : ByteBuf, V : Any> StreamCodec<B, V>.toOptional(): StreamCodec<B, Optional<V>> = ByteBufCodecs.optional(this)
