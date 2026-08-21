@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.serialization.codec

import com.mojang.datafixers.kinds.App
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.MapCodec
import com.mojang.serialization.MapLike
import com.mojang.serialization.RecordBuilder
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.lib.registry.RegistryKey
import hiiragi283.lib.text.Text
import hiiragi283.lib.util.DFUPair
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.Ior
import hiiragi283.lib.util.Option
import hiiragi283.lib.util.kotlin
import hiiragi283.lib.util.some
import java.util.UUID
import java.util.stream.Stream
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.enums.enumEntries
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.UUIDUtil
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.util.ExtraCodecs
import org.apache.commons.lang3.math.Fraction

/**
 * Hiiragi Seriesで使用される[Codec]と[MapCodec]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
data object HTCodecs {
    @JvmField
    val FRACTION: Codec<Fraction> = xor(Codec.STRING, Codec.INT)
        .xmap(
            { either: Either<String, Int> -> either.fold(Fraction::getFraction) { Fraction.getFraction(it, 1) } },
            { fraction: Fraction ->
                when (fraction.denominator) {
                    1 -> Either.Right(fraction.numerator)
                    else -> Either.Left(fraction.toString())
                }
            },
        )

    @JvmField
    val TEXT: Codec<Text> = ComponentSerialization.CODEC

    @JvmField
    val UUID: Codec<UUID> = UUIDUtil.CODEC

    @JvmStatic
    fun <K : Any, V : Any> mapOf(keyCodec: Codec<K>, valueCodec: Codec<V>): Codec<Map<K, V>> = Codec.unboundedMap(keyCodec, valueCodec)

    /**
     * [Option]でラップされた[Codec]を作成します。
     *
     * 参照 : [DataFixerUpper - ExtraCodecs.optionalEmptyMap][ExtraCodecs.optionalEmptyMap]
     */
    @JvmStatic
    fun <A : Any> option(codec: Codec<A>): Codec<Option<A>> = OptionCodec(codec)

    /**
     * @suppress
     */
    @JvmRecord
    private data class OptionCodec<A : Any>(private val codec: Codec<A>) : Codec<Option<A>> {
        override fun <T> encode(input: Option<A>, ops: DynamicOps<T>, prefix: T): DataResult<T> = input.fold(
            { DataResult.success(ops.emptyMap()) },
            { codec.encode(it, ops, prefix) },
        )

        private fun <T> isEmptyMap(ops: DynamicOps<T>, input: T): Boolean = ops.getMap(input).result().kotlin.fold(
            { false },
            { it.entries().findAny().isEmpty },
        )

        override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<DFUPair<Option<A>, T>> = when {
            isEmptyMap(ops, input) -> DataResult.success(DFUPair.of(Option.none(), input))
            else -> codec.decode(ops, input).map { pair: DFUPair<A, T> -> pair.mapFirst { it.some() } }
        }
    }

    @JvmStatic
    fun <A : Any, B : Any> pair(first: Codec<A>, second: Codec<B>): MapCodec<Pair<A, B>> = recordMap { instance ->
        instance.group(
            first.fieldOf("first").forGetter(Pair<A, B>::first),
            second.fieldOf("second").forGetter(Pair<A, B>::second),
        ).apply(instance, ::Pair)
    }

    @JvmStatic
    fun <A : Any, B : Any> mapPair(first: MapCodec<A>, second: MapCodec<B>): MapCodec<Pair<A, B>> = recordMap { instance ->
        instance.group(
            first.forGetter(Pair<A, B>::first),
            second.forGetter(Pair<A, B>::second),
        ).apply(instance, ::Pair)
    }

    /**
     * [Either]の[Codec]を作成します。
     * @param A 左側の値となるクラス
     * @param B 右側の値となるクラス
     * @param left 左側の値の[Codec]
     * @param right 右側の値の[Codec]
     * @see Codec.either
     */
    @JvmStatic
    fun <A, B> either(left: Codec<A>, right: Codec<B>): Codec<Either<A, B>> = HTEitherCodec(left, right, false)

    /**
     * [Either]の[Codec]を作成します。
     * @param A 左側の値となるクラス
     * @param B 右側の値となるクラス
     * @param left 左側の値の[Codec]
     * @param right 右側の値の[Codec]
     * @see Codec.xor
     */
    @JvmStatic
    fun <A, B> xor(left: Codec<A>, right: Codec<B>): Codec<Either<A, B>> = HTEitherCodec(left, right, true)

    /**
     * @suppress
     * @see com.mojang.serialization.codecs.EitherCodec
     * @see com.mojang.serialization.codecs.XorCodec
     */
    @JvmRecord
    private data class HTEitherCodec<A, B>(val left: Codec<A>, val right: Codec<B>, val isStrict: Boolean) : Codec<Either<A, B>> {
        override fun <T : Any> encode(input: Either<A, B>, ops: DynamicOps<T>, prefix: T): DataResult<T> = input.fold(
            { left.encode(it, ops, prefix) },
            { right.encode(it, ops, prefix) },
        )

        override fun <T : Any> decode(ops: DynamicOps<T>, input: T): DataResult<DFUPair<Either<A, B>, T>> {
            val leftRead: DataResult<DFUPair<Either<A, B>, T>> = left.decode(ops, input).map { it.mapFirst { Either.Left(it) } }
            val rightRead: DataResult<DFUPair<Either<A, B>, T>> = right.decode(ops, input).map { it.mapFirst { Either.Right(it) } }
            val leftResult: Option<DFUPair<Either<A, B>, T>> = leftRead.result().kotlin
            val rightResult: Option<DFUPair<Either<A, B>, T>> = rightRead.result().kotlin
            if (isStrict && (leftResult.isSome() && rightResult.isSome())) {
                return DataResult.error({ "Both alternatives read successfully, can not pick the correct one; first: ${leftResult.getOrNull()} second: ${rightResult.getOrNull()}" }, leftResult.getOrNull())
            }
            if (leftResult.isSome()) {
                return leftRead
            }
            if (rightResult.isSome()) {
                return rightRead
            }
            return leftRead.apply2({ _, second -> second }, rightRead)
        }
    }

    /**
     * [Ior]の[MapCodec]を作成します。
     * @param A 左側の値となるクラス
     * @param B 右側の値となるクラス
     * @param left 左側の値の[MapCodec]
     * @param right 右側の値の[MapCodec]
     */
    @JvmStatic
    fun <A, B> ior(left: MapCodec<A>, right: MapCodec<B>): MapCodec<Ior<A, B>> = HTIorMapCodec(left, right)

    /**
     * @suppress
     */
    private data class HTIorMapCodec<A, B>(val left: MapCodec<A>, val right: MapCodec<B>) : MapCodec<Ior<A, B>>() {
        override fun <T : Any> keys(ops: DynamicOps<T>): Stream<T> = Stream.concat(left.keys(ops), right.keys(ops))

        override fun <T : Any> decode(ops: DynamicOps<T>, input: MapLike<T>): DataResult<Ior<A, B>> {
            val leftResult: DataResult<A> = left.decode(ops, input)
            val rightResult: DataResult<B> = right.decode(ops, input)

            val bothResult: DataResult<Ior<A, B>> = leftResult.flatMap { leftIn: A ->
                rightResult.map { rightIn: B -> Ior.Both(leftIn, rightIn) }
            }
            if (bothResult.isSuccess) return bothResult
            if (leftResult.isSuccess) {
                return when {
                    rightResult.isSuccess ->
                        leftResult.flatMap { leftIn: A ->
                            rightResult.map { rightIn: B -> Ior.Both(leftIn, rightIn) }
                        }
                    else -> leftResult.map { Ior.Left(it) }
                }
            } else {
                return when {
                    rightResult.isSuccess -> rightResult.map { Ior.Right(it) }
                    else ->
                        DataResult.error {
                            val leftError: String = leftResult.error().orElseThrow().message()
                            val rightError: String = rightResult.error().orElseThrow().message()
                            "Failed to parse ior. Left: $leftError; Right: $rightError;"
                        }
                }
            }
        }

        override fun <T : Any> encode(input: Ior<A, B>, ops: DynamicOps<T>, prefix: RecordBuilder<T>): RecordBuilder<T> = input.fold(
            { left.encode(it, ops, prefix) },
            { right.encode(it, ops, prefix) },
            { left: A, right: B ->
                this.left.encode(left, ops, prefix)
                this.right.encode(right, ops, prefix)
            },
        )
    }

    /**
     * @see net.neoforged.neoforge.common.util.NeoForgeExtraCodecs.dispatchMapOrElse
     */
    @JvmStatic
    fun <A : Any, E : Any, B : Any> dispatchOrElse(typeCodec: Codec<A>, typeGetter: (E) -> A, codecGetter: (A) -> MapCodec<out E>, fallbackCodec: Codec<B>): Codec<Either<E, B>> = xor(typeCodec.dispatch(typeGetter, codecGetter), fallbackCodec)

    /**
     * [Enum]の[Codec]を返します。
     * @param V [Enum]を継承したクラス
     * @param factory [V]を[String]に変換するブロック
     */
    @JvmStatic
    inline fun <reified V : Enum<V>> stringEnum(crossinline factory: (V) -> String?): Codec<V> = Codec.STRING.flatXmap<V>(
        { name: String -> enumEntries<V>().firstOrNull { factory(it) == name }?.let { DataResult.success(it) } ?: DataResult.error { "Unknown element name: $name" } },
        { value: V -> factory(value)?.let { DataResult.success(it) } ?: DataResult.error { "Element with unknown name: $value" } },
    )

    /**
     * [RecordCodecBuilder.mapCodec]を最適化した代替
     */
    @JvmStatic
    inline fun <O> recordMap(builder: (RecordCodecBuilder.Instance<O>) -> App<RecordCodecBuilder.Mu<O>, O>): MapCodec<O> {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        return RecordCodecBuilder.build(builder(RecordCodecBuilder.instance()))
    }

    /**
     * [RecordCodecBuilder.create]を最適化した代替
     */
    @JvmStatic
    inline fun <O> record(builder: (RecordCodecBuilder.Instance<O>) -> App<RecordCodecBuilder.Mu<O>, O>): Codec<O> {
        contract {
            callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
        }
        return recordMap(builder).codec()
    }

    //    Ranged    //

    /**
     * @see ExtraCodecs.intRangeWithMessage
     */
    @JvmStatic
    fun <N> numberRange(codec: Codec<N>, range: ClosedRange<N>): Codec<N> where N : Number, N : Comparable<N> = codec.validate { number: N ->
        when (number) {
            in range -> DataResult.success(number)
            else -> DataResult.error { "Value must be within range $range: $number" }
        }
    }

    /**
     * `0`以上の値を対象とする[Int]の[Codec]
     */
    @JvmField
    val NON_NEGATIVE_INT: Codec<Int> = ExtraCodecs.NON_NEGATIVE_INT

    /**
     * `0`以上の値を対象とする[Long]の[Codec]
     * @see mekanism.api.SerializerHelper.POSITIVE_LONG_CODEC
     */
    @JvmField
    val NON_NEGATIVE_LONG: Codec<Long> = numberRange(Codec.LONG, 0..Long.MAX_VALUE)

    /**
     * `0`以上の値を対象とする[Fraction]の[Codec]
     */
    @JvmField
    val NON_NEGATIVE_FRACTION: Codec<Fraction> = FRACTION.validate { fraction: Fraction ->
        when {
            fraction < Fraction.ZERO -> DataResult.error { "Value must be non-negative: $fraction" }
            else -> DataResult.success(fraction)
        }
    }

    /**
     * `1`以上の値を対象とする[Int]の[Codec]
     */
    @JvmField
    val POSITIVE_INT: Codec<Int> = ExtraCodecs.POSITIVE_INT

    /**
     * `1`以上の値を対象とする[Long]の[Codec]
     * @see mekanism.api.SerializerHelper.POSITIVE_LONG_CODEC
     */
    @JvmField
    val POSITIVE_LONG: Codec<Long> = numberRange(Codec.LONG, 1..Long.MAX_VALUE)

    //    Registry    //

    /**
     * 指定した[registryKey]から[ResourceKey]の[Codec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> resourceKey(registryKey: RegistryKey<T>): Codec<ResourceKey<T>> = ResourceKey.codec(registryKey)

    /**
     * 指定した[registryKey]から[TagKey]の[Codec]を返します。
     * @param T レジストリの要素のクラス
     * @param withHash 変換後の文字列の先頭に'#'をつけるかどうか
     */
    @JvmStatic
    fun <T : Any> tagKey(registryKey: RegistryKey<T>, withHash: Boolean): Codec<TagKey<T>> = when (withHash) {
        true -> TagKey.hashedCodec(registryKey)
        false -> TagKey.codec(registryKey)
    }

    /**
     * 指定した[registryKey]から[Holder]の[Codec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> holder(registryKey: RegistryKey<T>): Codec<Holder<T>> = RegistryFixedCodec.create(registryKey)

    /**
     * 指定した[registryKey]から[HolderSet]の[Codec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> holderSet(registryKey: RegistryKey<T>): Codec<HolderSet<T>> = RegistryCodecs.homogeneousList(registryKey)

    @JvmStatic
    fun <T : Any> holderSet(registryKey: RegistryKey<T>, element: Codec<T>): Codec<HolderSet<T>> = RegistryCodecs.homogeneousList(registryKey, element)
}
