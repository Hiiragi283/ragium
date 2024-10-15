package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
import net.minecraft.component.ComponentChanges
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.registry.tag.TagKey

class HTRecipeResult private constructor(private val entryList: RegistryEntryList<Item>, val count: Int, val components: ComponentChanges) {
    companion object {
        @JvmField
        val EMPTY = HTRecipeResult(RegistryEntryList.empty(), 0, ComponentChanges.EMPTY)

        @JvmField
        val CODEC: Codec<HTRecipeResult> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    RegistryCodecs
                        .entryList(RegistryKeys.ITEM, Registries.ITEM.codec)
                        .fieldOf("items")
                        .forGetter(HTRecipeResult::entryList),
                    Codec
                        .intRange(1, 99)
                        .optionalFieldOf("count", 1)
                        .forGetter(HTRecipeResult::count),
                    ComponentChanges.CODEC
                        .optionalFieldOf("components", ComponentChanges.EMPTY)
                        .forGetter(HTRecipeResult::components),
                ).apply(instance, ::HTRecipeResult)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTRecipeResult> = PacketCodec.tuple(
            PacketCodecs.registryEntryList(RegistryKeys.ITEM),
            HTRecipeResult::entryList,
            PacketCodecs.INTEGER,
            HTRecipeResult::count,
            ComponentChanges.PACKET_CODEC,
            HTRecipeResult::components,
            ::HTRecipeResult,
        )

        @JvmField
        val LIST_PACKET_CODEC: PacketCodec<RegistryByteBuf, List<HTRecipeResult>> = PACKET_CODEC.toList()

        @Suppress("DEPRECATION")
        @JvmStatic
        fun of(item: ItemConvertible, count: Int = 1, components: ComponentChanges = ComponentChanges.EMPTY): HTRecipeResult =
            when (val item1: Item = item.asItem()) {
                Items.AIR -> EMPTY
                else -> HTRecipeResult(RegistryEntryList.of(item1.registryEntry), count, components)
            }

        @JvmStatic
        fun of(stack: ItemStack): HTRecipeResult = of(stack.item, stack.count, stack.componentChanges)

        @JvmStatic
        fun of(tagKey: TagKey<Item>, count: Int = 1, components: ComponentChanges = ComponentChanges.EMPTY): HTRecipeResult =
            Registries.ITEM
                .getOrCreateEntryList(tagKey)
                .let { HTRecipeResult(it, count, components) }
    }

    val firstEntry: RegistryEntry<Item>?
        get() = entryList.firstOrNull()

    val firstItem: Item
        get() = firstEntry?.value() ?: Items.AIR

    fun toStack(): ItemStack = firstEntry?.let {
        ItemStack(it, count, components)
    } ?: ItemStack.EMPTY

    fun canAccept(other: ItemStack): Boolean = when {
        other.isEmpty -> true
        ItemStack.areItemsAndComponentsEqual(toStack(), other) -> true
        other.count + this.count > other.maxCount -> false
        else -> false
    }

    fun modifyStack(other: ItemStack): ItemStack {
        val stack: ItemStack = toStack()
        return when {
            !canAccept(other) -> other
            other.isEmpty -> stack
            other.count + this.count > other.maxCount -> other.apply { count = other.maxCount }
            ItemStack.areItemsAndComponentsEqual(stack, other) -> other.apply { count += stack.count }
            else -> other
        }
    }

    override fun toString(): String = "HTRecipeResult[count=$count, item=$entryList, components=$components]"
}
