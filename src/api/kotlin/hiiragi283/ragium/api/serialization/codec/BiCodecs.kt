package hiiragi283.ragium.api.serialization.codec

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.function.andThen
import hiiragi283.ragium.api.math.fraction
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs
import org.apache.commons.lang3.math.Fraction
import java.util.function.Function
import java.util.function.Supplier
import kotlin.enums.enumEntries

object BiCodecs {
    @JvmStatic
    fun <N> checkRange(min: N, max: N): (N) -> N where N : Number, N : Comparable<N> =
        Codec.checkRange(min, max).andThen(DataResult<N>::getOrThrow)::apply

    /**
     * 指定された[min]と[max]から[BiCodec]を返します。
     * @param min 範囲の最小値
     * @param max 範囲の最大値
     * @return [min]と[max]を含む範囲の値のみを通す[BiCodec]
     */
    @JvmStatic
    fun intRange(min: Int, max: Int): BiCodec<ByteBuf, Int> = BiCodec.INT.validate(checkRange(min, max))

    /**
     * 指定された[min]と[max]から[BiCodec]を返します。
     * @param min 範囲の最小値
     * @param max 範囲の最大値
     * @return [min]と[max]を含む範囲の値のみを通す[BiCodec]
     */
    @JvmStatic
    fun longRange(min: Long, max: Long): BiCodec<ByteBuf, Long> = BiCodec.LONG.validate(checkRange(min, max))

    /**
     * 指定された[min]と[max]から[BiCodec]を返します。
     * @param min 範囲の最小値
     * @param max 範囲の最大値
     * @return [min]と[max]を含む範囲の値のみを通す[BiCodec]
     */
    @JvmStatic
    fun fractionRange(min: Fraction, max: Fraction): BiCodec<ByteBuf, Fraction> = FRACTION.validate(checkRange(min, max))

    /**
     * `0`以上の値を対象とする[Int]の[BiCodec]
     * @see net.minecraft.util.ExtraCodecs.NON_NEGATIVE_INT
     */
    @JvmField
    val NON_NEGATIVE_INT: BiCodec<ByteBuf, Int> = intRange(0, Int.MAX_VALUE)

    /**
     * `0`以上の値を対象とする[Long]の[BiCodec]
     * @see mekanism.api.SerializerHelper.POSITIVE_LONG_CODEC
     */
    @JvmField
    val NON_NEGATIVE_LONG: BiCodec<ByteBuf, Long> = longRange(0, Long.MAX_VALUE)

    /**
     * `1`以上の値を対象とする[Int]の[BiCodec]
     * @see net.minecraft.util.ExtraCodecs.POSITIVE_INT
     */
    @JvmField
    val POSITIVE_INT: BiCodec<ByteBuf, Int> = intRange(1, Int.MAX_VALUE)

    /**
     * `1`以上の値を対象とする[Long]の[BiCodec]
     * @see mekanism.api.SerializerHelper.POSITIVE_NONZERO_LONG_CODEC
     */
    @JvmField
    val POSITIVE_LONG: BiCodec<ByteBuf, Long> = longRange(1, Long.MAX_VALUE)

    @JvmField
    val FRACTION: BiCodec<ByteBuf, Fraction> = either(BiCodec.STRING, BiCodec.INT).xmap(
        { either: Either<String, Int> -> either.map(Fraction::getFraction, ::fraction) },
        { fraction: Fraction ->
            if (fraction.denominator == 1) {
                Either.right(fraction.numerator)
            } else {
                Either.left(fraction.toString())
            }
        },
    )

    /**
     * `0`以上の値を対象とする[Fraction]の[BiCodec]
     */
    @JvmField
    val NON_NEGATIVE_FRACTION: BiCodec<ByteBuf, Fraction> = FRACTION.validate { fraction: Fraction ->
        check(fraction > Fraction.ZERO) { "Value must be non-negative: $fraction" }
        fraction
    }

    /**
     * 指定された[keyCodec], [valueCodec]に基づいて，[Map]の[BiCodec]を返します。
     * @param K [Map]のキーとなるクラス
     * @param V [Map]の値となるクラス
     * @param keyCodec [K]を対象とする[BiCodec]
     * @param valueCodec [V]を対象とする[BiCodec]
     * @return [Map]の[BiCodec]
     */
    @JvmStatic
    fun <B : ByteBuf, K : Any, V : Any> mapOf(keyCodec: BiCodec<in B, K>, valueCodec: BiCodec<in B, V>): BiCodec<B, Map<K, V>> = BiCodec.of(
        Codec.unboundedMap(keyCodec.codec, valueCodec.codec),
        ByteBufCodecs.map(::HashMap, keyCodec.streamCodec, valueCodec.streamCodec),
    )

    /**
     * 指定された[first], [second]に基づいて，[Either]の[BiCodec]を返します。
     * @param first [F]を対象とする[BiCodec]
     * @param second [S]を対象とする[BiCodec]
     * @return [Either]の[BiCodec]
     */
    @JvmStatic
    fun <B : ByteBuf, F : Any, S : Any> either(first: BiCodec<in B, F>, second: BiCodec<in B, S>): BiCodec<B, Either<F, S>> = BiCodec.of(
        Codec.either(first.codec, second.codec),
        ByteBufCodecs.either(first.streamCodec, second.streamCodec),
    )

    /**
     * 指定された[first], [second]に基づいて，[Either]の[BiCodec]を返します。
     * @param first [F]を対象とする[BiCodec]
     * @param second [S]を対象とする[BiCodec]
     * @return [Either]の[BiCodec]
     */
    @JvmStatic
    fun <B : ByteBuf, F : Any, S : Any> xor(first: BiCodec<in B, F>, second: BiCodec<in B, S>): BiCodec<B, Either<F, S>> = BiCodec.of(
        Codec.xor(first.codec, second.codec),
        ByteBufCodecs.either(first.streamCodec, second.streamCodec),
    )

    @JvmStatic
    inline fun <reified V : Enum<V>> enum(values: Supplier<Array<V>>): BiCodec<ByteBuf, V> =
        NON_NEGATIVE_INT.flatXmap({ value: Int -> values.get()[value] }, Enum<V>::ordinal)

    @JvmStatic
    inline fun <reified V : Enum<V>> stringEnum(factory: Function<V, String>): BiCodec<ByteBuf, V> = BiCodec.STRING.flatXmap(
        { name: String -> enumEntries<V>().first { factory.apply(it) == name } },
        factory,
    )

    @JvmStatic
    fun <B : ByteBuf, V : Any> lazy(delegate: () -> BiCodec<B, V>): BiCodec<B, V> = BiCodec.of(
        Codec.lazyInitialized(delegate.andThen(BiCodec<B, V>::codec)),
        NeoForgeStreamCodecs.lazy(delegate.andThen(BiCodec<B, V>::streamCodec)),
    )

    /**
     * 指定された[instance]を常に返す[BiCodec]を返します。
     */
    @JvmStatic
    fun <B : ByteBuf, V : Any> unit(instance: V): BiCodec<B, V> = BiCodec.of(Codec.unit(instance), StreamCodec.unit(instance))
}
