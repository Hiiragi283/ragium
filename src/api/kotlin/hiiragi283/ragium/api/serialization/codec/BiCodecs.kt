package hiiragi283.ragium.api.serialization.codec

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import java.util.function.Function
import java.util.function.Supplier
import kotlin.enums.enumEntries

object BiCodecs {
    /**
     * 指定された[BiCodec]を，別の[BiCodec]に変換します。
     * @param N [Number]を継承したクラス
     * @param range 値の範囲
     * @return [range]に範囲を制限された[BiCodec]
     */
    fun <B : ByteBuf, N> BiCodec<B, N>.ranged(range: ClosedRange<N>): BiCodec<B, N> where N : Number, N : Comparable<N> =
        this.validate { value: N ->
            check(value in range) { "Value $value outside of range [$range]" }
            value
        }

    /**
     * `0`以上の値を対象とする[Int]の[BiCodec]
     */
    @JvmField
    val NON_NEGATIVE_INT: BiCodec<ByteBuf, Int> = BiCodec.INT.ranged(0..Int.MAX_VALUE)

    /**
     * `0`以上の値を対象とする[Long]の[BiCodec]
     * @see mekanism.api.SerializerHelper.POSITIVE_LONG_CODEC
     */
    @JvmField
    val NON_NEGATIVE_LONG: BiCodec<ByteBuf, Long> = BiCodec.LONG.ranged(0..Long.MAX_VALUE)

    /**
     * `1`以上の値を対象とする[Int]の[BiCodec]
     */
    @JvmField
    val POSITIVE_INT: BiCodec<ByteBuf, Int> = BiCodec.INT.ranged(1..Int.MAX_VALUE)

    /**
     * `1`以上の値を対象とする[Long]の[BiCodec]
     * @see mekanism.api.SerializerHelper.POSITIVE_NONZERO_LONG_CODEC
     */
    @JvmField
    val POSITIVE_LONG: BiCodec<ByteBuf, Long> = BiCodec.LONG.ranged(1..Long.MAX_VALUE)

    /**
     * `0f`以上の値を対象とする[Float]の[BiCodec]
     */
    @JvmField
    val POSITIVE_FLOAT: BiCodec<ByteBuf, Float> = BiCodec.FLOAT.ranged(0f..Float.MAX_VALUE)

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
    fun <B : ByteBuf, T : Any> withAlternative(primal: BiCodec<in B, T>, secondary: BiCodec<in B, T>): BiCodec<B, T> = BiCodec.of(
        NeoForgeExtraCodecs.withAlternative(primal.codec, secondary.codec),
        AlternativeStreamCodec(primal.streamCodec, secondary.streamCodec),
    )

    @JvmStatic
    inline fun <reified V : Enum<V>> enum(values: Supplier<Array<V>>): BiCodec<ByteBuf, V> =
        NON_NEGATIVE_INT.flatXmap({ value: Int -> values.get()[value] }, Enum<V>::ordinal)

    @JvmStatic
    inline fun <reified V : Enum<V>> stringEnum(factory: Function<V, String>): BiCodec<ByteBuf, V> = BiCodec.STRING.flatXmap(
        { name: String -> enumEntries<V>().first { factory.apply(it) == name } },
        factory,
    )

    /**
     * @see NeoForgeExtraCodecs.AlternativeCodec
     */
    @JvmRecord
    private data class AlternativeStreamCodec<B : Any, V : Any>(val primal: StreamCodec<in B, V>, val secondary: StreamCodec<in B, V>) :
        StreamCodec<B, V> {
        override fun decode(buffer: B): V = runCatching { primal.decode(buffer) }.getOrElse { secondary.decode(buffer) }

        override fun encode(buffer: B, value: V) {
            runCatching { primal.encode(buffer, value) }.onFailure { secondary.encode(buffer, value) }
        }
    }
}
