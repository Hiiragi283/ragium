package hiiragi283.ragium.api.data

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.Registry
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack

object BiCodecs {
    @JvmField
    val NON_NEGATIVE_INT: BiCodec<ByteBuf, Int> = BiCodec.of(ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.INT)

    @JvmField
    val POSITIVE_INT: BiCodec<ByteBuf, Int> = BiCodec.of(ExtraCodecs.POSITIVE_INT, ByteBufCodecs.INT)

    @JvmField
    val POSITIVE_FLOAT: BiCodec<ByteBuf, Float> = BiCodec.of(ExtraCodecs.POSITIVE_FLOAT, ByteBufCodecs.FLOAT)

    @JvmField
    val RL: BiCodec<ByteBuf, ResourceLocation> = BiCodec.of(ResourceLocation.CODEC, ResourceLocation.STREAM_CODEC)

    @JvmField
    val COLOR: BiCodec<ByteBuf, DyeColor> = BiCodec.of(DyeColor.CODEC, DyeColor.STREAM_CODEC)

    @JvmField
    val COMPONENT_PATCH: BiCodec<RegistryFriendlyByteBuf, DataComponentPatch> =
        BiCodec.of(DataComponentPatch.CODEC, DataComponentPatch.STREAM_CODEC)

    @JvmStatic
    fun <B : ByteBuf, K : Any, V : Any> mapOf(keyCodec: BiCodec<in B, K>, valueCodec: BiCodec<in B, V>): BiCodec<B, Map<K, V>> = BiCodec.of(
        Codec.unboundedMap(keyCodec.codec, valueCodec.codec),
        ByteBufCodecs.map(::HashMap, keyCodec.streamCodec, valueCodec.streamCodec),
    )

    @JvmStatic
    fun <B : ByteBuf, F : Any, S : Any> either(first: BiCodec<in B, F>, second: BiCodec<in B, S>): BiCodec<B, Either<F, S>> = BiCodec.of(
        Codec.either(first.codec, second.codec),
        ByteBufCodecs.either(first.streamCodec, second.streamCodec),
    )

    @JvmStatic
    private val ITEM_STACK_NON_EMPTY: BiCodec<RegistryFriendlyByteBuf, ItemStack> = BiCodec.of(ItemStack.CODEC, ItemStack.STREAM_CODEC)

    @JvmStatic
    private val ITEM_STACK: BiCodec<RegistryFriendlyByteBuf, ItemStack> = BiCodec.of(
        ItemStack.OPTIONAL_CODEC,
        ItemStack.OPTIONAL_STREAM_CODEC,
    )

    @JvmStatic
    fun itemStack(allowEmpty: Boolean): BiCodec<RegistryFriendlyByteBuf, ItemStack> = when (allowEmpty) {
        true -> ITEM_STACK
        false -> ITEM_STACK_NON_EMPTY
    }

    // Registry
    @JvmStatic
    fun <T : Any> resourceKey(registryKey: ResourceKey<out Registry<T>>): BiCodec<ByteBuf, ResourceKey<T>> =
        BiCodec.of(ResourceKey.codec(registryKey), ResourceKey.streamCodec(registryKey))

    @JvmStatic
    fun <T : Any> tagKey(registryKey: ResourceKey<out Registry<T>>): BiCodec<ByteBuf, TagKey<T>> = BiCodec.of(
        TagKey.hashedCodec(registryKey),
        ResourceLocation.STREAM_CODEC.map(
            { id: ResourceLocation -> TagKey.create(registryKey, id) },
            TagKey<T>::location,
        ),
    )

    @JvmStatic
    fun <T : Any> registryBased(registry: Registry<T>): BiCodec<RegistryFriendlyByteBuf, T> =
        BiCodec.of(registry.byNameCodec(), ByteBufCodecs.registry(registry.key()))

    @JvmStatic
    fun <T : Any> holder(registryKey: ResourceKey<out Registry<T>>): BiCodec<RegistryFriendlyByteBuf, Holder<T>> =
        BiCodec.of(RegistryFixedCodec.create(registryKey), ByteBufCodecs.holderRegistry(registryKey))

    @JvmStatic
    fun <T : Any> holderSet(registryKey: ResourceKey<out Registry<T>>): BiCodec<RegistryFriendlyByteBuf, HolderSet<T>> =
        BiCodec.of(RegistryCodecs.homogeneousList(registryKey), ByteBufCodecs.holderSet(registryKey))
}
