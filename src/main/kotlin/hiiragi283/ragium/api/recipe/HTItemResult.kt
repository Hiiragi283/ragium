package hiiragi283.ragium.api.recipe

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.codec.RagiumCodecs
import hiiragi283.ragium.api.extension.isPopulated
import hiiragi283.ragium.api.extension.registryEntry
import net.minecraft.component.ComponentChanges
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryFixedCodec
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import kotlin.jvm.optionals.getOrNull

sealed class HTItemResult(val count: Int, val components: ComponentChanges) {
    companion object {
        @JvmStatic
        private val simpleFilter: (HTItemResult) -> Boolean =
            { entry: HTItemResult -> entry.count == 1 && entry.components.isEmpty }

        @JvmField
        val CODEC: Codec<HTItemResult> = Codec
            .either(ItemEntry.CODEC, TagEntry.CODEC)
            .xmap(Either<HTItemResult, HTItemResult>::unwrap) { result: HTItemResult ->
                when (result) {
                    is ItemEntry -> Either.left(result)
                    is TagEntry -> Either.right(result)
                }
            }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTItemResult> =
            object : PacketCodec<RegistryByteBuf, HTItemResult> {
                private val entryCodec: PacketCodec<RegistryByteBuf, RegistryEntry<Item>> =
                    PacketCodecs.registryEntry(RegistryKeys.ITEM)

                override fun decode(buf: RegistryByteBuf): HTItemResult = when (buf.readBoolean()) {
                    true -> {
                        val tagKey: TagKey<Item> = TagKey.of(RegistryKeys.ITEM, Identifier.PACKET_CODEC.decode(buf))
                        val count: Int = buf.readVarInt()
                        val components: ComponentChanges = ComponentChanges.PACKET_CODEC.decode(buf)
                        TagEntry(tagKey, count, components)
                    }

                    false -> {
                        val entry: RegistryEntry<Item> = entryCodec.decode(buf)
                        val count: Int = buf.readVarInt()
                        val components: ComponentChanges = ComponentChanges.PACKET_CODEC.decode(buf)
                        ItemEntry(entry, count, components)
                    }
                }

                override fun encode(buf: RegistryByteBuf, value: HTItemResult) {
                    when (value) {
                        is ItemEntry -> {
                            buf.writeBoolean(false)
                            entryCodec.encode(buf, value.firstEntry)
                        }

                        is TagEntry -> {
                            buf.writeBoolean(true)
                            Identifier.PACKET_CODEC.encode(buf, value.tagKey.id)
                        }
                    }
                    buf.writeVarInt(value.count)
                    ComponentChanges.PACKET_CODEC.encode(buf, value.components)
                }
            }

        //    Item    //

        @JvmStatic
        fun ofItem(entry: RegistryEntry<Item>, count: Int = 1, components: ComponentChanges = ComponentChanges.EMPTY): HTItemResult =
            ItemEntry(entry, count, components)

        @JvmStatic
        fun ofItem(item: ItemConvertible, count: Int = 1, components: ComponentChanges = ComponentChanges.EMPTY): HTItemResult =
            ofItem(item.registryEntry, count, components)

        @JvmStatic
        fun fromStack(stack: ItemStack): HTItemResult = ofItem(stack.registryEntry, stack.count, stack.componentChanges)

        //    Tag    //

        @JvmStatic
        fun fromTag(tagKey: TagKey<Item>, count: Int = 1, components: ComponentChanges = ComponentChanges.EMPTY): HTItemResult =
            TagEntry(tagKey, count, components)
    }

    abstract val firstEntry: RegistryEntry<Item>
    val stack: ItemStack get() = ItemStack(firstEntry, count, components)

    abstract val isEmpty: Boolean

    /**
     * 指定した[other]にマージできるか判定します。
     */
    fun canMerge(other: ItemStack): Boolean = when {
        this.isEmpty -> false
        other.isEmpty -> true
        other.count + this.count > other.maxCount -> false
        ItemStack.areItemsAndComponentsEqual(stack, other) -> true
        else -> false
    }

    /**
     * 指定した[other]にマージします。
     * @return [other]とマージした[ItemStack]
     */
    fun merge(other: ItemStack): ItemStack = when {
        this.isEmpty -> other
        other.isEmpty -> stack
        other.count + this.count > other.maxCount -> other
        ItemStack.areItemsAndComponentsEqual(stack, other) -> other.apply { count += stack.count }
        else -> other
    }

    //    ItemEntry    //

    internal class ItemEntry(override val firstEntry: RegistryEntry<Item>, count: Int, components: ComponentChanges) :
        HTItemResult(count, components) {
        companion object {
            @JvmField
            val CODEC: Codec<ItemEntry> = RagiumCodecs.simpleOrComplex(
                RegistryFixedCodec.of(RegistryKeys.ITEM).xmap(::ItemEntry, ItemEntry::firstEntry),
                RecordCodecBuilder.create { instance ->
                    instance
                        .group(
                            RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(ItemEntry::firstEntry),
                            Codec.intRange(0, Int.MAX_VALUE).optionalFieldOf("count", 1).forGetter(ItemEntry::count),
                            ComponentChanges.CODEC
                                .optionalFieldOf("components", ComponentChanges.EMPTY)
                                .forGetter(ItemEntry::components),
                        ).apply(instance, ::ItemEntry)
                },
                simpleFilter,
            )
        }

        private constructor(entry: RegistryEntry<Item>) : this(entry, 1, ComponentChanges.EMPTY)

        override val isEmpty: Boolean = stack.isEmpty
    }

    //    TagEntry    //

    internal class TagEntry(val tagKey: TagKey<Item>, count: Int = 1, components: ComponentChanges = ComponentChanges.EMPTY) :
        HTItemResult(count, components) {
        companion object {
            @JvmField
            val CODEC: Codec<TagEntry> = RagiumCodecs.simpleOrComplex(
                TagKey.codec(RegistryKeys.ITEM).xmap(::TagEntry, TagEntry::tagKey),
                RecordCodecBuilder.create { instance ->
                    instance
                        .group(
                            TagKey.codec(RegistryKeys.ITEM).fieldOf("tag").forGetter(TagEntry::tagKey),
                            Codec.intRange(0, Int.MAX_VALUE).optionalFieldOf("count", 1).forGetter(TagEntry::count),
                            ComponentChanges.CODEC
                                .optionalFieldOf("components", ComponentChanges.EMPTY)
                                .forGetter(TagEntry::components),
                        ).apply(instance, ::TagEntry)
                },
                simpleFilter,
            )
        }

        override val firstEntry: RegistryEntry<Item>
            get() = Registries.ITEM
                .getEntryList(tagKey)
                ?.getOrNull()
                ?.firstOrNull()
                ?: Registries.ITEM.defaultEntry.get()

        override val isEmpty: Boolean
            get() = !tagKey.isPopulated()
    }
}
