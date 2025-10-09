package hiiragi283.ragium.api.codec

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.RegistryKey
import hiiragi283.ragium.api.extension.createTagKey
import hiiragi283.ragium.api.extension.ranged
import io.netty.buffer.ByteBuf
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.Registry
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.UUIDUtil
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs
import java.util.UUID
import java.util.function.Supplier

object BiCodecs {
    /**
     * `0`以上の値を対象とする[Int]の[BiCodec]
     */
    @JvmField
    val NON_NEGATIVE_INT: BiCodec<ByteBuf, Int> = BiCodec.INT.ranged(0..Int.MAX_VALUE)

    /**
     * `0`以上の値を対象とする[Long]の[BiCodec]
     * @see [mekanism.api.SerializerHelper.POSITIVE_LONG_CODEC]
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
     * @see [mekanism.api.SerializerHelper.POSITIVE_NONZERO_LONG_CODEC]
     */
    @JvmField
    val POSITIVE_LONG: BiCodec<ByteBuf, Long> = BiCodec.LONG.ranged(1..Long.MAX_VALUE)

    /**
     * `0f`以上の値を対象とする[Float]の[BiCodec]
     */
    @JvmField
    val POSITIVE_FLOAT: BiCodec<ByteBuf, Float> = BiCodec.FLOAT.ranged(0f..Float.MAX_VALUE)

    /**
     * [ResourceLocation]の[BiCodec]
     */
    @JvmField
    val RL: BiCodec<ByteBuf, ResourceLocation> = BiCodec.of(ResourceLocation.CODEC, ResourceLocation.STREAM_CODEC)

    /**
     * [DyeColor]の[BiCodec]
     */
    @JvmField
    val COLOR: BiCodec<ByteBuf, DyeColor> = BiCodec.of(DyeColor.CODEC, DyeColor.STREAM_CODEC)

    /**
     * [DataComponentPatch]の[BiCodec]
     */
    @JvmField
    val COMPONENT_PATCH: BiCodec<RegistryFriendlyByteBuf, DataComponentPatch> =
        BiCodec.of(DataComponentPatch.CODEC, DataComponentPatch.STREAM_CODEC)

    /**
     * [Direction]の[BiCodec]
     */
    @JvmField
    val DIRECTION: BiCodec<ByteBuf, Direction> = BiCodec.of(Direction.CODEC, Direction.STREAM_CODEC)

    /**
     * [InteractionHand]の[BiCodec]
     */
    @JvmField
    val HAND: BiCodec<ByteBuf, InteractionHand> = enum(InteractionHand::values)

    /**
     * [Component]の[BiCodec]
     */
    @JvmField
    val TEXT: BiCodec<RegistryFriendlyByteBuf, Component> =
        BiCodec.of(ComponentSerialization.CODEC, ComponentSerialization.STREAM_CODEC)

    /**
     * [java.util.UUID]の[BiCodec]
     */
    @JvmField
    val UUID: BiCodec<ByteBuf, UUID> = BiCodec.of(UUIDUtil.CODEC, UUIDUtil.STREAM_CODEC)

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
        NON_NEGATIVE_INT.xmap({ value: Int -> values.get()[value] }, Enum<V>::ordinal)

    @JvmStatic
    inline fun <reified V> stringEnum(values: Supplier<Array<V>>): BiCodec<FriendlyByteBuf, V> where V : Enum<V>, V : StringRepresentable =
        BiCodec.of(StringRepresentable.fromEnum(values), NeoForgeStreamCodecs.enumCodec(V::class.java))

    @JvmStatic
    private val ITEM_STACK_NON_EMPTY: BiCodec<RegistryFriendlyByteBuf, ItemStack> = BiCodec.of(ItemStack.CODEC, ItemStack.STREAM_CODEC)

    @JvmStatic
    private val ITEM_STACK: BiCodec<RegistryFriendlyByteBuf, ItemStack> = BiCodec.of(
        ItemStack.OPTIONAL_CODEC,
        ItemStack.OPTIONAL_STREAM_CODEC,
    )

    /**
     * [ItemStack]の[BiCodec]を返します。
     * @param allowEmpty [ItemStack.EMPTY]を許容するかどうか
     */
    @JvmStatic
    fun itemStack(allowEmpty: Boolean): BiCodec<RegistryFriendlyByteBuf, ItemStack> = when (allowEmpty) {
        true -> ITEM_STACK
        false -> ITEM_STACK_NON_EMPTY
    }

    @JvmStatic
    private val FLUID_STACK_NON_EMPTY: BiCodec<RegistryFriendlyByteBuf, FluidStack> = BiCodec.of(FluidStack.CODEC, FluidStack.STREAM_CODEC)

    @JvmStatic
    private val FLUID_STACK: BiCodec<RegistryFriendlyByteBuf, FluidStack> = BiCodec.of(
        FluidStack.OPTIONAL_CODEC,
        FluidStack.OPTIONAL_STREAM_CODEC,
    )

    /**
     * [FluidStack]の[BiCodec]を返します。
     * @param allowEmpty [FluidStack.EMPTY]を許容するかどうか
     */
    @JvmStatic
    fun fluidStack(allowEmpty: Boolean): BiCodec<RegistryFriendlyByteBuf, FluidStack> = when (allowEmpty) {
        true -> FLUID_STACK
        false -> FLUID_STACK_NON_EMPTY
    }

    @JvmStatic
    fun ingredient(allowEmpty: Boolean): BiCodec<RegistryFriendlyByteBuf, Ingredient> = BiCodec.of(
        when (allowEmpty) {
            true -> Ingredient.CODEC
            false -> Ingredient.CODEC_NONEMPTY
        },
        Ingredient.CONTENTS_STREAM_CODEC,
    )

    // Registry

    /**
     * 指定された[registryKey]から[ResourceKey]の[BiCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> resourceKey(registryKey: RegistryKey<T>): BiCodec<ByteBuf, ResourceKey<T>> =
        BiCodec.of(ResourceKey.codec(registryKey), ResourceKey.streamCodec(registryKey))

    /**
     * 指定された[registryKey]から[TagKey]の[BiCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> tagKey(registryKey: RegistryKey<T>): BiCodec<ByteBuf, TagKey<T>> = BiCodec.of(
        TagKey.hashedCodec(registryKey),
        ResourceLocation.STREAM_CODEC.map(registryKey::createTagKey, TagKey<T>::location),
    )

    /**
     * 指定された[registry]から[T]の[BiCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> registryBased(registry: Registry<T>): BiCodec<RegistryFriendlyByteBuf, T> =
        BiCodec.of(registry.byNameCodec(), ByteBufCodecs.registry(registry.key()))

    /**
     * 指定された[registryKey]から[Holder]の[BiCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> holder(registryKey: RegistryKey<T>): BiCodec<RegistryFriendlyByteBuf, Holder<T>> =
        BiCodec.of(RegistryFixedCodec.create(registryKey), ByteBufCodecs.holderRegistry(registryKey))

    /**
     * 指定された[registryKey]から[HolderSet]の[BiCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> holderSet(registryKey: RegistryKey<T>): BiCodec<RegistryFriendlyByteBuf, HolderSet<T>> =
        BiCodec.of(RegistryCodecs.homogeneousList(registryKey), ByteBufCodecs.holderSet(registryKey))
}
