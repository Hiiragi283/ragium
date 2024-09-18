package hiiragi283.ragium.common.recipe

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.common.util.mapCast
import hiiragi283.ragium.common.util.toList
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

sealed class HTRecipeResult(val count: Int) {
    abstract val value: Item

    init {
        check(count > 0) { "Invalid result count; $count" }
    }

    abstract fun writeBuf(buf: RegistryByteBuf)

    fun toStack(): ItemStack = ItemStack(value, count)

    fun canAccept(other: ItemStack): Boolean = when {
        other.isEmpty -> true
        ItemStack.areItemsAndComponentsEqual(toStack(), other) -> true
        else -> false
    }

    fun modifyStack(other: ItemStack): ItemStack {
        val stack: ItemStack = toStack()
        return when {
            !canAccept(other) -> other
            other.isEmpty -> stack
            ItemStack.areItemsAndComponentsEqual(stack, other) -> other.apply { count += stack.count }
            else -> other
        }
    }

    companion object {
        @JvmField
        val CODEC: Codec<HTRecipeResult> =
            Codec
                .xor(ItemImpl.CODEC, TagImpl.CODEC)
                .xmap(Either<ItemImpl, TagImpl>::mapCast) { result: HTRecipeResult ->
                    when (result) {
                        is ItemImpl -> Either.left(result)
                        is TagImpl -> Either.right(result)
                    }
                }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTRecipeResult> =
            PacketCodec.of(HTRecipeResult::writeBuf) { buf: RegistryByteBuf ->
                val id: Identifier = buf.readIdentifier()
                val count: Int = buf.readVarInt()
                when (buf.readBoolean()) {
                    true -> {
                        val item: Item = Registries.ITEM.get(id)
                        item(item, count)
                    }

                    false -> {
                        val tagKey: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, id)
                        tag(tagKey, count)
                    }
                }
            }

        @JvmField
        val LIST_PACKET_CODEC: PacketCodec<RegistryByteBuf, List<HTRecipeResult>> = PACKET_CODEC.toList()

        @JvmStatic
        fun item(item: ItemConvertible, count: Int = 1): HTRecipeResult = ItemImpl(item, count)

        @JvmStatic
        fun tag(tagKey: TagKey<Item>, count: Int = 1): HTRecipeResult = TagImpl(tagKey, count)
    }

    //    Item    //

    private class ItemImpl(item: ItemConvertible, count: Int) :
        HTRecipeResult(count),
        ItemConvertible by item {
        companion object {
            @JvmField
            val CODEC: Codec<ItemImpl> =
                RecordCodecBuilder.create { instance ->
                    instance
                        .group(
                            Registries.ITEM.codec
                                .fieldOf("item")
                                .forGetter(ItemImpl::asItem),
                            Codec.INT
                                .orElse(1)
                                .fieldOf("count")
                                .forGetter(ItemImpl::count),
                        ).apply(instance, ::ItemImpl)
                }
        }

        override val value: Item

        init {
            val item1: Item = item.asItem()
            check(item1 != Items.AIR) { "Could not accept Items.AIR as a result!" }
            value = item1
        }

        override fun writeBuf(buf: RegistryByteBuf) {
            buf.writeIdentifier(Registries.ITEM.getId(asItem()))
            buf.writeVarInt(count)
            buf.writeBoolean(true)
        }
    }

    private class TagImpl(private val tagKey: TagKey<Item>, count: Int) : HTRecipeResult(count) {
        companion object {
            @JvmField
            val CODEC: Codec<TagImpl> =
                RecordCodecBuilder.create { instance ->
                    instance
                        .group(
                            TagKey.unprefixedCodec(RegistryKeys.ITEM).fieldOf("tag").forGetter(TagImpl::tagKey),
                            Codec.INT
                                .orElse(1)
                                .fieldOf("count")
                                .forGetter(TagImpl::count),
                        ).apply(instance, ::TagImpl)
                }
        }

        override val value: Item
            get() {
                val item: Item? =
                    Registries.ITEM
                        .iterateEntries(tagKey)
                        .map(RegistryEntry<Item>::value)
                        .getOrNull(0)
                return checkNotNull(item) { "TagKey; $tagKey has no elements!" }
            }

        override fun writeBuf(buf: RegistryByteBuf) {
            buf.writeIdentifier(tagKey.id)
            buf.writeVarInt(count)
            buf.writeBoolean(false)
        }
    }
}
