package hiiragi283.lib.serialization.network

import hiiragi283.lib.registry.RegistryKey
import hiiragi283.lib.tag.createTagKey
import hiiragi283.lib.text.Text
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.Ior
import hiiragi283.lib.util.java
import hiiragi283.lib.util.kotlin
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.UUIDUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.util.ByIdMap
import java.util.Optional
import java.util.UUID

/**
 * Hiiragi Seriesで使用される[StreamCodec]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTStreamCodecs {
    @JvmField
    val TEXT: StreamCodec<RegistryFriendlyByteBuf, Text> = ComponentSerialization.STREAM_CODEC

    @JvmField
    val UUID: StreamCodec<ByteBuf, UUID> = UUIDUtil.STREAM_CODEC

    /**
     * [Map]の[StreamCodec]を作成します。
     * @param B パケットのクラス
     * @param K キーとなるクラス
     * @param V 値となるクラス
     * @param keyCodec キーの[StreamCodec]
     * @param valueCodec 値の[StreamCodec]
     */
    @JvmStatic
    fun <B : ByteBuf, K : Any, V : Any> mapOf(
        keyCodec: StreamCodec<in B, K>,
        valueCodec: StreamCodec<in B, V>
    ): StreamCodec<B, Map<K, V>> = ByteBufCodecs.map(::LinkedHashMap, keyCodec, valueCodec)

    /**
     * [Optional]でラップされた[StreamCodec]を作成します。
     */
    @JvmStatic
    fun <B : ByteBuf, V : Any> optional(codec: StreamCodec<in B, V>): StreamCodec<B, Optional<V>> =
        ByteBufCodecs.optional(codec)

    /**
     * [Pair]の[StreamCodec]を作成します。
     * @param BUF パケットのクラス
     * @param A 左側の値となるクラス
     * @param B 右側の値となるクラス
     * @param left 左側の値の[StreamCodec]
     * @param right 右側の値の[StreamCodec]
     */
    @JvmStatic
    fun <BUF : ByteBuf, A : Any, B : Any> pair(
        left: StreamCodec<in BUF, A>,
        right: StreamCodec<in BUF, B>
    ): StreamCodec<BUF, Pair<A, B>> = StreamCodec.composite(
        left,
        Pair<A, B>::first,
        right,
        Pair<A, B>::second,
        ::Pair
    )

    /**
     * [Either]の[StreamCodec]を作成します。
     * @param BUF パケットのクラス
     * @param A 左側の値となるクラス
     * @param B 右側の値となるクラス
     * @param left 左側の値の[StreamCodec]
     * @param right 右側の値の[StreamCodec]
     * @see ByteBufCodecs.either
     */
    @JvmStatic
    fun <BUF : ByteBuf, A : Any, B : Any> either(
        left: StreamCodec<in BUF, A>,
        right: StreamCodec<in BUF, B>
    ): StreamCodec<BUF, Either<A, B>> = ByteBufCodecs.either(left, right).map({ it.kotlin }, { it.java })

    /**
     * [Ior]の[StreamCodec]を作成します。
     * @param BUF パケットのクラス
     * @param A 左側の値となるクラス
     * @param B 右側の値となるクラス
     * @param left 左側の値の[StreamCodec]
     * @param right 右側の値の[StreamCodec]
     */
    @JvmStatic
    fun <BUF : ByteBuf, A : Any, B : Any> ior(
        left: StreamCodec<in BUF, A>,
        right: StreamCodec<in BUF, B>
    ): StreamCodec<BUF, Ior<A, B>> = either(
        either(left, right),
        pair(left, right)
    ).map(
        { either: Either<Either<A, B>, Pair<A, B>> -> either.fold(Ior.Companion::fromEither, Ior.Companion::fromPair) },
        Ior<A, B>::unwrap
    )

    /**
     * [Enum]の[StreamCodec]を返します。
     * @param V [Enum]を継承したクラス
     */
    @JvmStatic
    inline fun <reified V : Enum<V>> enum(
        strategy: ByIdMap.OutOfBoundsStrategy = ByIdMap.OutOfBoundsStrategy.WRAP
    ): StreamCodec<ByteBuf, V> = ByteBufCodecs.idMapper(
        ByIdMap.continuous(Enum<V>::ordinal, V::class.java.enumConstants, strategy),
        Enum<V>::ordinal
    )

    //    Registry    //

    /**
     * 指定した[registryKey]から[ResourceKey]の[StreamCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> resourceKey(registryKey: RegistryKey<T>): StreamCodec<ByteBuf, ResourceKey<T>> =
        ResourceKey.streamCodec(registryKey)

    /**
     * [TagKey]の[StreamCodec]を作成します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> tagKey(registryKey: RegistryKey<T>): StreamCodec<ByteBuf, TagKey<T>> =
        Identifier.STREAM_CODEC.map(registryKey::createTagKey, TagKey<T>::location)

    /**
     * 指定した[registryKey]から[Holder]の[StreamCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> holder(registryKey: RegistryKey<T>): StreamCodec<RegistryFriendlyByteBuf, Holder<T>> =
        ByteBufCodecs.holderRegistry(registryKey)

    /**
     * 指定した[registryKey]から[HolderSet]の[StreamCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> holderSet(registryKey: RegistryKey<T>): StreamCodec<RegistryFriendlyByteBuf, HolderSet<T>> =
        ByteBufCodecs.holderSet(registryKey)
}
