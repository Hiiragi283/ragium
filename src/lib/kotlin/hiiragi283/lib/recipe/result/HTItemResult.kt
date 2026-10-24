package hiiragi283.lib.recipe.result

import com.google.common.collect.Comparators
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.collection.Nel
import hiiragi283.lib.data.buildDataPatch
import hiiragi283.lib.item.component.buildItemEnchantments
import hiiragi283.lib.recipe.display.HTDisplayableContent
import hiiragi283.lib.recipe.display.HTSizedSlotDisplay
import hiiragi283.lib.recipe.display.HTSlotDisplayHelper
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.codec.nelOrElement
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.serialization.network.nelOf
import hiiragi283.lib.serialization.network.optionalOf
import hiiragi283.lib.util.DFUEither
import hiiragi283.lib.util.fold
import hiiragi283.ragium.api.RagiumConfigAccess
import hiiragi283.ragium.api.RagiumRegistries
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import java.util.Optional
import kotlin.jvm.optionals.getOrElse

/**
 * アイテムの完成品を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTItemResult(val entry: Entry, val count: Int) :
    HTRecipeResult<ItemStack>,
    HTDisplayableContent {
    companion object {
        @JvmField
        val MAP_CODEC: MapCodec<HTItemResult> = HTCodecs.recordMap { instance ->
            instance.group(
                Entry.MAP_CODEC.forGetter(HTItemResult::entry),
                HTCodecs.POSITIVE_INT.fieldOf(HTConstants.COUNT).orElse(1).forGetter(HTItemResult::count),
            ).apply(instance, ::HTItemResult)
        }

        @JvmField
        val CODEC: Codec<HTItemResult> = Codec.withAlternative(MAP_CODEC.codec(), Entry.MAP_CODEC.codec()) {
            it.toResult()
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
    override fun create(): ItemStack = entry.create(count)

    override fun getId(): Identifier? = entry.getId()

    override fun display(): SlotDisplay = HTSizedSlotDisplay(entry.display(), count)

    //    Entry    //

    interface Entry {
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
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Entry> = ByteBufCodecs.registry(
                RagiumRegistries.Keys.ITEM_RESULT_TYPE,
            ).dispatch(Entry::type, HTItemResultType<*>::streamCodec)
        }

        fun type(): HTItemResultType<*>

        fun create(count: Int = 1): ItemStack

        fun toResult(count: Int = 1): HTItemResult = HTItemResult(this, count)

        fun getId(): Identifier?

        fun display(): SlotDisplay
    }

    @JvmRecord
    data class SimpleEntry @JvmOverloads constructor(val item: Holder<Item>, val components: DataComponentPatch = DataComponentPatch.EMPTY) : Entry {
        companion object {
            @JvmField
            val CODEC: MapCodec<SimpleEntry> = HTCodecs.recordMap { instance ->
                instance.group(
                    Item.CODEC.fieldOf(HTConstants.ID).forGetter(SimpleEntry::item),
                    DataComponentPatch.CODEC.optionalFieldOf(
                        HTConstants.COMPONENTS,
                        DataComponentPatch.EMPTY,
                    ).forGetter(SimpleEntry::components),
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

        override fun create(count: Int): ItemStack = ItemStack(item, count, components)

        override fun getId(): Identifier = item.getKeyOrThrow().identifier()

        override fun display(): SlotDisplay = HTSlotDisplayHelper.create(ItemStackTemplate(item, 1, components))
    }

    @JvmRecord
    data class TagEntry(val tag: HolderSet<Item>) : Entry {
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
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, TagEntry> =
                HTStreamCodecs.holderSet(Registries.ITEM).map(::TagEntry, TagEntry::tag)

            @JvmField
            val TYPE: HTItemResultType<TagEntry> = HTItemResultType(CODEC, STREAM_CODEC)

            /**
             * @since 26.1.4
             */
            @JvmField
            val HOLDER_COMPARATOR: Comparator<Holder<Item>> = run {
                compareBy(RagiumConfigAccess.INSTANCE.modIdComparator, ResourceKey<Item>::identifier)
                    .let(Comparators::emptiesLast)
                    .let { compareBy(it, Holder<Item>::unwrapKey) }
            }
        }

        override fun type(): HTItemResultType<*> = TYPE

        override fun create(count: Int): ItemStack = tag
            .asSequence()
            .sortedWith(HOLDER_COMPARATOR)
            .firstOrNull()
            ?.let { ItemStack(it, count) }
            ?: ItemStack.EMPTY

        override fun getId(): Identifier = tag.unwrapKey().orElseThrow().location()

        override fun display(): SlotDisplay = tag
            .asSequence()
            .sortedWith(HOLDER_COMPARATOR)
            .firstOrNull()
            .let(HTSlotDisplayHelper::create)
    }

    /**
     * @since 26.1.6
     */
    @JvmRecord
    data class EnchantedBookEntry(val enchantments: Nel<EnchantmentValue>) : Entry {

        @JvmRecord
        data class EnchantmentValue(val enchantment: Holder<Enchantment>, private val level: Optional<Int>) {
            companion object {
                @JvmField
                val FLAT_CODEC: Codec<EnchantmentValue> = Enchantment.CODEC.xmap(
                    ::EnchantmentValue,
                    EnchantmentValue::enchantment,
                )

                @JvmField
                val NESTED_CODEC: Codec<EnchantmentValue> = HTCodecs.record { instance ->
                    instance.group(
                        Enchantment.CODEC.fieldOf(HTConstants.ID).forGetter(EnchantmentValue::enchantment),
                        HTCodecs.POSITIVE_INT.optionalFieldOf("level").forGetter(EnchantmentValue::level),
                    ).apply(instance, ::EnchantmentValue)
                }

                @JvmField
                val CODEC: Codec<EnchantmentValue> = Codec.either(FLAT_CODEC, NESTED_CODEC).xmap(
                    { DFUEither.unwrap(it) },
                    { value: EnchantmentValue ->
                        if (value.level.isEmpty) DFUEither.left(value) else DFUEither.right(value)
                    },
                )

                @JvmField
                val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, EnchantmentValue> = StreamCodec.composite(
                    Enchantment.STREAM_CODEC,
                    EnchantmentValue::enchantment,
                    ByteBufCodecs.VAR_INT.optionalOf(),
                    EnchantmentValue::level,
                    ::EnchantmentValue,
                )
            }

            constructor(enchantment: Holder<Enchantment>) : this(enchantment, Optional.empty())

            constructor(enchantment: Holder<Enchantment>, level: Int) : this(enchantment, Optional.of(level))

            val fixedLevel: Int get() = this.level.getOrElse { this.enchantment.value().maxLevel }
        }

        companion object {
            @JvmField
            val CODEC: MapCodec<EnchantedBookEntry> = EnchantmentValue.CODEC
                .nelOrElement()
                .fieldOf("enchantments")
                .xmap(::EnchantedBookEntry, EnchantedBookEntry::enchantments)

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, EnchantedBookEntry> = EnchantmentValue.STREAM_CODEC
                .nelOf()
                .map(::EnchantedBookEntry, EnchantedBookEntry::enchantments)

            @JvmField
            val TYPE: HTItemResultType<EnchantedBookEntry> = HTItemResultType(CODEC, STREAM_CODEC)
        }

        override fun type(): HTItemResultType<*> = TYPE

        override fun create(count: Int): ItemStack {
            val stack = ItemStack(Items.ENCHANTED_BOOK, count)
            EnchantmentHelper.setEnchantments(
                stack,
                buildItemEnchantments {
                    for (value: EnchantmentValue in enchantments) {
                        set(value.enchantment, value.fixedLevel)
                    }
                },
            )
            return stack
        }

        override fun getId(): Identifier? = enchantments.head.enchantment.key?.identifier()

        override fun display(): SlotDisplay = HTSlotDisplayHelper.create(
            ItemStackTemplate(
                Items.ENCHANTED_BOOK,
                1,
                buildDataPatch {
                    set(
                        DataComponents.STORED_ENCHANTMENTS,
                        buildItemEnchantments {
                            for (value: EnchantmentValue in enchantments) {
                                set(value.enchantment, value.fixedLevel)
                            }
                        },
                    )
                },
            ),
        )
    }

    /**
     * @since 26.1.7
     */
    @JvmRecord
    data class WithFallbackEntry(val base: Entry, val fallback: Entry) : Entry {
        companion object {
            @JvmField
            val CODEC: MapCodec<WithFallbackEntry> = HTCodecs.recordMap { instance ->
                instance.group(
                    Entry.MAP_CODEC.fieldOf("base").forGetter(WithFallbackEntry::base),
                    Entry.MAP_CODEC.fieldOf("fallback").forGetter(WithFallbackEntry::fallback),
                ).apply(instance, ::WithFallbackEntry)
            }

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, WithFallbackEntry> = StreamCodec.composite(
                Entry.STREAM_CODEC,
                WithFallbackEntry::base,
                Entry.STREAM_CODEC,
                WithFallbackEntry::fallback,
                ::WithFallbackEntry,
            )

            @JvmField
            val TYPE: HTItemResultType<WithFallbackEntry> = HTItemResultType(CODEC, STREAM_CODEC)
        }

        override fun type(): HTItemResultType<*> = TYPE

        override fun create(count: Int): ItemStack = base.create(count).takeUnless(ItemStack::isEmpty) ?: fallback.create(count)

        override fun getId(): Identifier? = base.getId() ?: fallback.getId()

        override fun display(): SlotDisplay = base.display().takeUnless { it is SlotDisplay.Empty } ?: fallback.display()
    }
}

//    Extensions    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
fun HTItemResult?.createOrEmpty(): ItemStack = this?.create() ?: ItemStack.EMPTY

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
fun Optional<HTItemResult>.createOrEmpty(): ItemStack = this.fold(ItemStack::EMPTY, HTItemResult::create)
