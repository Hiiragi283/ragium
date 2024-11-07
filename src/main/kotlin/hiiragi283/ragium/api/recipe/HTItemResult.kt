package hiiragi283.ragium.api.recipe

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.entryComparator
import hiiragi283.ragium.api.extension.entryPacketCodec
import net.minecraft.component.ComponentChanges
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryEntryOwner
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.*
import java.util.function.Predicate
import java.util.stream.Stream

class HTItemResult(val entry: RegistryEntry<Item>, val count: Int = 1, val components: ComponentChanges = ComponentChanges.EMPTY) {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemResult> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    Registries.ITEM.entryCodec
                        .fieldOf("item")
                        .forGetter(HTItemResult::entry),
                    Codec
                        .intRange(1, Int.MAX_VALUE)
                        .optionalFieldOf("count", 1)
                        .forGetter(HTItemResult::count),
                    ComponentChanges.CODEC
                        .optionalFieldOf("components", ComponentChanges.EMPTY)
                        .forGetter(HTItemResult::components),
                ).apply(instance, ::HTItemResult)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTItemResult> = PacketCodec.tuple(
            Registries.ITEM.entryPacketCodec,
            HTItemResult::entry,
            PacketCodecs.VAR_INT,
            HTItemResult::count,
            ComponentChanges.PACKET_CODEC,
            HTItemResult::components,
            ::HTItemResult,
        )
    }

    init {
        check(count > 0) { "Non-Negative count required!" }
    }

    constructor(item: ItemConvertible, count: Int = 1, components: ComponentChanges = ComponentChanges.EMPTY) : this(
        item.asItem().registryEntry,
        count,
        components,
    )

    constructor(stack: ItemStack) : this(stack.registryEntry, stack.count, stack.componentChanges)

    @Deprecated("only used in runtime recipe registration")
    constructor(tagKey: TagKey<Item>, count: Int = 1) : this(TagRegistryEntry(tagKey), count)

    val item: Item
        get() = entry.value()
    val stack: ItemStack
        get() = ItemStack(entry, count, components)

    fun canMerge(other: ItemStack): Boolean = when {
        other.isEmpty -> true
        ItemStack.areItemsAndComponentsEqual(stack, other) -> true
        other.count + this.count > other.maxCount -> false
        else -> false
    }

    fun merge(other: ItemStack): ItemStack = when {
        !canMerge(other) -> other
        other.isEmpty -> stack
        other.count + this.count > other.maxCount -> other
        ItemStack.areItemsAndComponentsEqual(stack, other) -> other.apply { count += stack.count }
        else -> other
    }

    //    TagRegistryEntry    //

    private class TagRegistryEntry(val tagKey: TagKey<Item>) : RegistryEntry<Item> {
        private val firstEntry: RegistryEntry<Item>
            get() = Registries.ITEM
                .getEntryList(tagKey)
                .orElseThrow { NoSuchElementException("TagKey; ${tagKey.id} has no entry!") }
                .sortedWith(entryComparator(Registries.ITEM))
                .first()

        override fun value(): Item = firstEntry.value()

        override fun hasKeyAndValue(): Boolean = firstEntry.hasKeyAndValue()

        override fun matchesId(id: Identifier): Boolean = firstEntry.matchesId(id)

        override fun streamTags(): Stream<TagKey<Item>> = firstEntry.streamTags()

        override fun getKeyOrValue(): Either<RegistryKey<Item>, Item> = firstEntry.keyOrValue

        override fun getKey(): Optional<RegistryKey<Item>> = firstEntry.key

        override fun getType(): RegistryEntry.Type = firstEntry.type

        override fun ownerEquals(owner: RegistryEntryOwner<Item>): Boolean = firstEntry.ownerEquals(owner)

        override fun isIn(tag: TagKey<Item>): Boolean = firstEntry.isIn(tag)

        @Deprecated("Deprecated in Java")
        override fun matches(entry: RegistryEntry<Item>): Boolean = firstEntry.matches(entry)

        override fun matches(predicate: Predicate<RegistryKey<Item>>): Boolean = firstEntry.matches(predicate)

        override fun matchesKey(key: RegistryKey<Item>): Boolean = firstEntry.matchesKey(key)
    }
}
