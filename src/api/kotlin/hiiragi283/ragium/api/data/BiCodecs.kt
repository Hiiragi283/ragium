package hiiragi283.ragium.api.data

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

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
    val COMPONENT_PATCH: BiCodec<RegistryFriendlyByteBuf, DataComponentPatch> =
        BiCodec.of(DataComponentPatch.CODEC, DataComponentPatch.STREAM_CODEC)

    @JvmField
    val FLUID_STACK: BiCodec<RegistryFriendlyByteBuf, FluidStack> =
        BiCodec.of(FluidStack.CODEC, FluidStack.STREAM_CODEC)

    @JvmField
    val ITEM_STACK: BiCodec<RegistryFriendlyByteBuf, ItemStack> = BiCodec.of(ItemStack.CODEC, ItemStack.STREAM_CODEC)

    @JvmStatic
    fun <B : ByteBuf, F : Any, S : Any> either(first: BiCodec<in B, F>, second: BiCodec<in B, S>): BiCodec<B, Either<F, S>> = BiCodec.of(
        Codec.either(first.codec, second.codec),
        ByteBufCodecs.either(first.streamCodec, second.streamCodec),
    )

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
    fun <T : Any> holder(registryKey: ResourceKey<out Registry<T>>): BiCodec<RegistryFriendlyByteBuf, Holder<T>> =
        BiCodec.of(RegistryFixedCodec.create(registryKey), ByteBufCodecs.holderRegistry(registryKey))
}
