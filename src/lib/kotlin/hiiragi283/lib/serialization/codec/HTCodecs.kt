@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.serialization.codec

import com.mojang.datafixers.kinds.App
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.lib.registry.RegistryKey
import hiiragi283.lib.text.Text
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.Ior
import hiiragi283.lib.util.Option
import hiiragi283.lib.util.java
import hiiragi283.lib.util.kotlin
import java.util.UUID
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

/**
 * Hiiragi Seriesで使用される[Codec]と[MapCodec]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTCodecs {
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
    fun <A : Any> option(codec: Codec<A>): Codec<Option<A>> = ExtraCodecs.optionalEmptyMap(codec).xmap({ it.kotlin }, { it.java })

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
    fun <A : Any, B : Any> either(left: Codec<A>, right: Codec<B>): Codec<Either<A, B>> = Codec.either(left, right).xmap({ it.kotlin }, { it.java })

    /**
     * [Either]の[Codec]を作成します。
     * @param A 左側の値となるクラス
     * @param B 右側の値となるクラス
     * @param left 左側の値の[Codec]
     * @param right 右側の値の[Codec]
     * @see Codec.xor
     */
    @JvmStatic
    fun <A : Any, B : Any> xor(left: Codec<A>, right: Codec<B>): Codec<Either<A, B>> = Codec.xor(left, right).xmap({ it.kotlin }, { it.java })

    /**
     * [Either]の[Codec]を作成します。
     * @param A 左側の値となるクラス
     * @param B 右側の値となるクラス
     * @param left 左側の値の[Codec]
     * @param right 右側の値の[Codec]
     * @see Codec.mapEither
     */
    @JvmStatic
    fun <A : Any, B : Any> mapEither(left: MapCodec<A>, right: MapCodec<B>): MapCodec<Either<A, B>> = Codec.mapEither(left, right).xmap({ it.kotlin }, { it.java })

    /**
     * [Ior]の[MapCodec]を作成します。
     * @param A 左側の値となるクラス
     * @param B 右側の値となるクラス
     * @param left 左側の値の[MapCodec]
     * @param right 右側の値の[MapCodec]
     */
    @JvmStatic
    fun <A : Any, B : Any> ior(left: MapCodec<A>, right: MapCodec<B>): MapCodec<Ior<A, B>> = mapEither(
        mapEither(left, right),
        mapPair(left, right),
    ).xmap(
        { either: Either<Either<A, B>, Pair<A, B>> -> either.fold(Ior.Companion::fromEither, Ior.Companion::fromPair) },
        Ior<A, B>::unwrap,
    )

    /*private data class HTIorMapCodec<A, B>(val left: MapCodec<A>, val right: MapCodec<B>) : MapCodec<Ior<A, B>>() {
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
    }*/

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
     * `0`以上の値を対象とする[Int]の[Codec]
     */
    @JvmField
    val NON_NEGATIVE_INT: Codec<Int> = ExtraCodecs.NON_NEGATIVE_INT

    /**
     * `1`以上の値を対象とする[Int]の[Codec]
     */
    @JvmField
    val POSITIVE_INT: Codec<Int> = ExtraCodecs.POSITIVE_INT

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
