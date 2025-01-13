package hiiragi283.ragium.api.recipe

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.asHolder
import hiiragi283.ragium.api.extension.simpleOrComplex
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.RegistryFixedCodec
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

sealed class HTItemResult(val count: Int, val components: DataComponentPatch) {
    companion object {
        @JvmStatic
        private val simpleFilter: (HTItemResult) -> Boolean =
            { entry: HTItemResult -> entry.count == 1 && entry.components.isEmpty }

        @JvmField
        val CODEC: Codec<HTItemResult> =
            Codec
                .either(ItemEntry.CODEC, TagEntry.CODEC)
                .xmap(Either<HTItemResult, HTItemResult>::unwrap) { result: HTItemResult ->
                    when (result) {
                        is ItemEntry -> Either.left(result)
                        is TagEntry -> Either.right(result)
                    }
                }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemResult> =
            object : StreamCodec<RegistryFriendlyByteBuf, HTItemResult> {
                private val entryCodec: StreamCodec<RegistryFriendlyByteBuf, Holder<Item>> =
                    ByteBufCodecs.holderRegistry(Registries.ITEM)
                private val holderSetCodec: StreamCodec<RegistryFriendlyByteBuf, HolderSet<Item>> =
                    ByteBufCodecs.holderSet(Registries.ITEM)

                override fun decode(buf: RegistryFriendlyByteBuf): HTItemResult = when (buf.readBoolean()) {
                    true -> {
                        val holderSet: HolderSet<Item> = holderSetCodec.decode(buf)
                        val count: Int = buf.readVarInt()
                        val components: DataComponentPatch = DataComponentPatch.STREAM_CODEC.decode(buf)
                        TagEntry(holderSet, count, components)
                    }

                    false -> {
                        val entry: Holder<Item> = entryCodec.decode(buf)
                        val count: Int = buf.readVarInt()
                        val components: DataComponentPatch = DataComponentPatch.STREAM_CODEC.decode(buf)
                        ItemEntry(entry, count, components)
                    }
                }

                override fun encode(buf: RegistryFriendlyByteBuf, value: HTItemResult) {
                    when (value) {
                        is ItemEntry -> {
                            buf.writeBoolean(false)
                            entryCodec.encode(buf, value.firstEntry)
                        }

                        is TagEntry -> {
                            buf.writeBoolean(true)
                            holderSetCodec.encode(buf, value.holderSet)
                        }
                    }
                    buf.writeVarInt(value.count)
                    DataComponentPatch.STREAM_CODEC.encode(buf, value.components)
                }
            }

        //    Item    //

        @JvmStatic
        fun ofItem(entry: Holder<Item>, count: Int = 1, components: DataComponentPatch = DataComponentPatch.EMPTY): HTItemResult =
            ItemEntry(entry, count, components)

        @JvmStatic
        fun ofItem(item: ItemLike, count: Int = 1, components: DataComponentPatch = DataComponentPatch.EMPTY): HTItemResult =
            ofItem(item.asHolder(), count, components)

        @JvmStatic
        fun fromStack(stack: ItemStack): HTItemResult = ofItem(stack.itemHolder, stack.count, stack.componentsPatch)

        //    Tag    //

        @JvmStatic
        fun fromTag(
            tagKey: TagKey<Item>,
            provider: HolderLookup.Provider,
            count: Int = 1,
            components: DataComponentPatch = DataComponentPatch.EMPTY,
        ): HTItemResult = fromTag(tagKey, provider.lookupOrThrow(Registries.ITEM), count, components)

        @JvmStatic
        fun fromTag(
            tagKey: TagKey<Item>,
            lookup: HolderLookup.RegistryLookup<Item>,
            count: Int = 1,
            components: DataComponentPatch = DataComponentPatch.EMPTY,
        ): HTItemResult = TagEntry(lookup.getOrThrow(tagKey), count, components)
    }

    abstract val firstEntry: Holder<Item>

    val stack: ItemStack get() = ItemStack(firstEntry, count, components)
    val isEmpty: Boolean
        get() = firstEntry.value() == Items.AIR

    /**
     * 指定した[other]にマージできるか判定します。
     */
    fun canMerge(other: ItemStack): Boolean = when {
        this.isEmpty -> false
        other.isEmpty -> true
        other.count + this.count > other.maxStackSize -> false
        ItemStack.isSameItemSameComponents(stack, other) -> true
        else -> false
    }

    /**
     * 指定した[other]にマージします。
     * @return [other]とマージした[ItemStack]
     */
    fun merge(other: ItemStack): ItemStack = when {
        this.isEmpty -> other
        other.isEmpty -> stack
        other.count + this.count > other.maxStackSize -> other
        ItemStack.isSameItemSameComponents(stack, other) -> other.apply { count += stack.count }
        else -> other
    }

    //    ItemEntry    //

    internal class ItemEntry(override val firstEntry: Holder<Item>, count: Int, components: DataComponentPatch) :
        HTItemResult(count, components) {
        companion object {
            @JvmField
            val CODEC: Codec<ItemEntry> =
                simpleOrComplex(
                    RegistryFixedCodec.create(Registries.ITEM).xmap(::ItemEntry, ItemEntry::firstEntry),
                    RecordCodecBuilder.create { instance ->
                        instance
                            .group(
                                RegistryFixedCodec.create(Registries.ITEM).fieldOf("item").forGetter(ItemEntry::firstEntry),
                                Codec.intRange(0, Int.MAX_VALUE).optionalFieldOf("count", 1).forGetter(ItemEntry::count),
                                DataComponentPatch.CODEC
                                    .optionalFieldOf("components", DataComponentPatch.EMPTY)
                                    .forGetter(ItemEntry::components),
                            ).apply(instance, ::ItemEntry)
                    },
                    simpleFilter,
                )
        }

        private constructor(entry: Holder<Item>) : this(entry, 1, DataComponentPatch.EMPTY)
    }

    //    TagEntry    //

    internal class TagEntry(val holderSet: HolderSet<Item>, count: Int = 1, components: DataComponentPatch = DataComponentPatch.EMPTY) :
        HTItemResult(count, components) {
        companion object {
            @JvmField
            val CODEC: Codec<TagEntry> =
                simpleOrComplex(
                    RegistryCodecs.homogeneousList(Registries.ITEM).xmap(::TagEntry, TagEntry::holderSet),
                    RecordCodecBuilder.create { instance ->
                        instance
                            .group(
                                RegistryCodecs
                                    .homogeneousList(Registries.ITEM)
                                    .fieldOf("tag")
                                    .forGetter(TagEntry::holderSet),
                                Codec.intRange(0, Int.MAX_VALUE).optionalFieldOf("count", 1).forGetter(TagEntry::count),
                                DataComponentPatch.CODEC
                                    .optionalFieldOf("components", DataComponentPatch.EMPTY)
                                    .forGetter(TagEntry::components),
                            ).apply(instance, ::TagEntry)
                    },
                    simpleFilter,
                )
        }

        override val firstEntry: Holder<Item>
            get() = holderSet.firstOrNull() ?: Items.AIR.asHolder()
    }
}
