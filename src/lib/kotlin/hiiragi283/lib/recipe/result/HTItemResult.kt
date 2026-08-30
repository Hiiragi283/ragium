package hiiragi283.lib.recipe.result

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.material.HTMaterialAccess
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.part.HTPart
import hiiragi283.lib.material.part.HTPartKey
import hiiragi283.lib.material.part.property.tagPrefix
import hiiragi283.lib.registry.asSupplier
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.registry.getOrNull
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.HTKeyLike
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.util.DFUEither
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.RagiumRegistries
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs

/**
 * アイテムの完成品を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTItemResult(val entry: Entry, val count: Int) : HTRecipeResult<ItemStack> {
    companion object {
        @JvmField
        val MAP_CODEC: MapCodec<HTItemResult> = HTCodecs.recordMap { instance ->
            instance.group(
                Entry.MAP_CODEC.forGetter(HTItemResult::entry),
                HTCodecs.POSITIVE_INT.fieldOf(HTConstants.COUNT).orElse(1).forGetter(HTItemResult::count),
            ).apply(instance, ::HTItemResult)
        }

        @JvmField
        val CODEC: Codec<HTItemResult> = Codec.withAlternative(MAP_CODEC.codec(), Entry.MAP_CODEC.codec()) { it.toResult() }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemResult> = StreamCodec.composite(
            Entry.STREAM_CODEC,
            HTItemResult::entry,
            ByteBufCodecs.VAR_INT,
            HTItemResult::count,
            ::HTItemResult,
        )
    }

    constructor(template: ItemStackTemplate) : this(SimpleEntry(template), template.count())

    constructor(stack: ItemStack) : this(SimpleEntry(stack), stack.count())

    /**
     * このインスタンスのコピーを作成します。
     * @param newCount 新しい個数
     */
    fun copyWithCount(newCount: Int): HTItemResult = HTItemResult(entry, newCount)

    /**
     * アイテムの完成品を作成します。
     */
    override fun create(): ItemStack = entry.create().copyWithCount(count)

    override fun getId(): Identifier = entry.getId()

    //    Entry    //

    interface Entry : HTIdLike {
        companion object {
            @JvmField
            val MAP_CODEC: MapCodec<Entry> = NeoForgeExtraCodecs.dispatchMapOrElse(
                RagiumRegistries.ITEM_RESULT_TYPE.byNameCodec(),
                Entry::type,
                HTItemResultType<*>::codec,
                SimpleEntry.CODEC,
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

        fun create(): ItemStack

        fun toResult(count: Int = 1): HTItemResult = HTItemResult(this, count)
    }

    @JvmRecord
    data class SimpleEntry @JvmOverloads constructor(val item: Holder<Item>, val components: DataComponentPatch = DataComponentPatch.EMPTY) :
        Entry,
        HTKeyLike<Item> {
        companion object {
            @JvmField
            val CODEC: MapCodec<SimpleEntry> = HTCodecs.recordMap { instance ->
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
            val TYPE: HTItemResultType<SimpleEntry> = HTItemResultType(CODEC, STREAM_CODEC)
        }

        constructor(template: ItemStackTemplate) : this(template.item(), template.components())

        constructor(stack: ItemStack) : this(stack.typeHolder(), stack.componentsPatch)

        override fun type(): HTItemResultType<*> = TYPE

        override fun create(): ItemStack = ItemStack(item, 1, components)

        override fun getKey(): ResourceKey<Item> = item.getKeyOrThrow()
    }

    @JvmInline
    value class TagEntry(val tag: HolderSet<Item>) : Entry {
        companion object {
            @JvmField
            val CODEC: MapCodec<TagEntry> = HTCodecs.holderSet(Registries.ITEM)
                .validate { holderSet: HolderSet<Item> ->
                    if (holderSet.unwrapKey().isEmpty) {
                        DataResult.error { "TagEntry only accepts tag set" }
                    } else {
                        DataResult.success(holderSet)
                    }
                }
                .fieldOf(HTConstants.TAG)
                .xmap(::TagEntry, TagEntry::tag)

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, TagEntry> = HTStreamCodecs.holderSet(Registries.ITEM).map(::TagEntry, TagEntry::tag)

            @JvmField
            val TYPE: HTItemResultType<TagEntry> = HTItemResultType(CODEC, STREAM_CODEC)
        }

        override fun type(): HTItemResultType<*> = TYPE

        override fun create(): ItemStack = tag
            .asSequence()
            .map(Holder<Item>::asSupplier)
            .sortedWith(RagiumConfig.SERVER.modIdComparator)
            .firstOrNull()
            ?.get()
            ?.let(::ItemStack)
            ?: ItemStack.EMPTY

        override fun getId(): Identifier = tag.unwrapKey().orElseThrow().location()
    }

    @JvmRecord
    data class MaterialEntry(val part: HTPart, val key: HTMaterialKey) : Entry {
        companion object {
            @JvmField
            val CODEC: MapCodec<MaterialEntry> = HTCodecs.recordMap { instance ->
                instance.group(
                    HTPart.CODEC.fieldOf("part").forGetter(MaterialEntry::part),
                    HTMaterialKey.CODEC.fieldOf(HTConstants.MATERIAL).forGetter(MaterialEntry::key),
                ).apply(instance, ::MaterialEntry)
            }

            @JvmField
            val TYPE: HTItemResultType<MaterialEntry> = HTItemResultType(CODEC)
        }

        constructor(part: HTPartKey, key: HTMaterialKey) : this(HTPart.getManager().getOrThrow(part), key)

        override fun type(): HTItemResultType<*> = TYPE

        override fun create(): ItemStack {
            val tagResult: ItemStack = part.tagPrefix
                ?.itemTagKey(key)
                ?.let(BuiltInRegistries.ITEM::getOrNull)
                ?.let(::TagEntry)
                ?.create()
                ?: ItemStack.EMPTY
            if (!tagResult.isEmpty) return tagResult
            return HTMaterialAccess.INSTANCE
                .getMaterialBlockOrItem(part.key, key)
                ?.item
                ?.toStack()
                ?: ItemStack.EMPTY
        }

        override fun getId(): Identifier = part.createId(key)
    }
}
