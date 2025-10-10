package hiiragi283.ragium.api.serialization.codec

import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.tag.createTagKey
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

object VanillaBiCodecs {
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
    val HAND: BiCodec<ByteBuf, InteractionHand> = BiCodecs.enum(InteractionHand::values)

    /**
     * [Component]の[BiCodec]
     */
    @JvmField
    val TEXT: BiCodec<RegistryFriendlyByteBuf, Component> =
        BiCodec.of(ComponentSerialization.CODEC, ComponentSerialization.STREAM_CODEC)

    /**
     * [UUID]の[BiCodec]
     */
    @JvmField
    val UUID: BiCodec<ByteBuf, UUID> = BiCodec.of(UUIDUtil.CODEC, UUIDUtil.STREAM_CODEC)

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

    @JvmStatic
    inline fun <reified V> stringEnum(values: Supplier<Array<V>>): BiCodec<FriendlyByteBuf, V> where V : Enum<V>, V : StringRepresentable =
        BiCodec.of(StringRepresentable.fromEnum(values), NeoForgeStreamCodecs.enumCodec(V::class.java))

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
