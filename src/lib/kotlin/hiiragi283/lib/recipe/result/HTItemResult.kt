package hiiragi283.lib.recipe.result

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.HTKeyLike
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.util.DFUEither
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.getOrElse
import hiiragi283.lib.util.right
import hiiragi283.ragium.api.RagiumRegistries
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs

@JvmRecord
data class HTItemResult(val entry: Entry, val count: Int) {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemResult> = HTCodecs.record { instance ->
            instance.group(
                Entry.MAP_CODEC.forGetter(HTItemResult::entry),
                HTCodecs.POSITIVE_INT.optionalFieldOf(HTConstants.COUNT, 1).forGetter(HTItemResult::count),
            ).apply(instance, ::HTItemResult)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemResult> = StreamCodec.composite(
            Entry.STREAM_CODEC,
            HTItemResult::entry,
            ByteBufCodecs.VAR_INT,
            HTItemResult::count,
            ::HTItemResult,
        )
    }

    fun create(): HTTextResult<ItemStackTemplate> = entry.create().map { it.withCount(count) }

    fun createStack(): HTTextResult<ItemStack> = create().map(ItemStackTemplate::create)

    fun createStackOrEmpty(): ItemStack = createStack().getOrElse { ItemStack.EMPTY }

    //    Entry    //

    interface Entry : HTIdLike {
        companion object {
            @JvmField
            val MAP_CODEC: MapCodec<Entry> = NeoForgeExtraCodecs.dispatchMapOrElse(
                RagiumRegistries.ITEM_RESULT_TYPE.byNameCodec(),
                Entry::type,
                HTItemResultType<*>::codec,
                SimpleEntry.MAP_CODEC,
            ).xmap(
                { DFUEither.unwrap(it) },
                { entry: Entry ->
                    when (entry) {
                        is SimpleEntry -> DFUEither.right(entry)
                        else -> DFUEither.left(entry)
                    }
                },
            )

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Entry> = ByteBufCodecs.registry(RagiumRegistries.Keys.ITEM_RESULT_TYPE).dispatch(Entry::type, HTItemResultType<*>::streamCodec)
        }

        fun type(): HTItemResultType<*>

        fun create(): HTTextResult<ItemStackTemplate>
    }

    @JvmRecord
    data class SimpleEntry(val item: Holder<Item>, val components: DataComponentPatch) :
        Entry,
        HTKeyLike<Item> {
        companion object {
            @JvmField
            val MAP_CODEC: MapCodec<SimpleEntry> = HTCodecs.recordMap { instance ->
                instance.group(
                    Item.CODEC.fieldOf(HTConstants.ID).forGetter(SimpleEntry::item),
                    DataComponentPatch.CODEC.optionalFieldOf(HTConstants.COMPONENTS, DataComponentPatch.EMPTY).forGetter(SimpleEntry::components),
                ).apply(instance, ::SimpleEntry)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SimpleEntry> = StreamCodec.composite(
                Item.STREAM_CODEC,
                SimpleEntry::item,
                DataComponentPatch.STREAM_CODEC,
                SimpleEntry::components,
                ::SimpleEntry,
            )

            @JvmField
            val TYPE: HTItemResultType<SimpleEntry> = HTItemResultType(MAP_CODEC, STREAM_CODEC)
        }

        override fun type(): HTItemResultType<*> = TYPE

        override fun create(): HTTextResult<ItemStackTemplate> = ItemStackTemplate(item, 1, components).right()

        override fun getKey(): ResourceKey<Item> = item.getKeyOrThrow()
    }

    @JvmInline
    value class TagEntry(val tagKey: TagKey<Item>) : Entry {
        companion object {
            @JvmField
            val CODEC: MapCodec<TagEntry> = HTCodecs.tagKey(Registries.ITEM, false).fieldOf(HTConstants.TAG).xmap(::TagEntry, TagEntry::tagKey)

            @JvmField
            val STREAM_CODEC: StreamCodec<ByteBuf, TagEntry> = HTStreamCodecs.tagKey(Registries.ITEM).map(::TagEntry, TagEntry::tagKey)

            @JvmField
            val TYPE: HTItemResultType<TagEntry> = HTItemResultType(CODEC, STREAM_CODEC.cast())
        }

        override fun type(): HTItemResultType<*> = TYPE

        override fun create(): HTTextResult<ItemStackTemplate> {
            TODO("Not yet implemented")
        }

        override fun getId(): Identifier = tagKey.location()
    }
}
