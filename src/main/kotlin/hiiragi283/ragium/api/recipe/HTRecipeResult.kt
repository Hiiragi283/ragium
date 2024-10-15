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
import net.minecraft.registry.entry.RegistryEntry

class HTRecipeResult private constructor(
    val entry: RegistryEntry<Item>,
    val count: Int,
    val components: ComponentChanges
) {
    companion object {
        @JvmField
        val EMPTY = HTRecipeResult(RegistryEntry.of(Items.AIR), 0, ComponentChanges.EMPTY)

        @JvmField
        val CODEC: Codec<HTRecipeResult> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    Registries.ITEM.entryCodec
                        .fieldOf("item")
                        .forGetter(HTRecipeResult::entry),
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
            PacketCodecs.registryCodec(Registries.ITEM.entryCodec),
            HTRecipeResult::entry,
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
                else -> HTRecipeResult(item1.registryEntry, count, components)
            }

        @JvmStatic
        fun of(stack: ItemStack): HTRecipeResult = of(stack.item, stack.count, stack.componentChanges)

        /*@JvmStatic
        fun of(tagKey: TagKey<Item>, count: Int = 1, components: ComponentChanges = ComponentChanges.EMPTY): HTRecipeResult =
            Registries.ITEM
                .getOrCreateEntryList(tagKey)
                .let { HTRecipeResult(it, count, components) }
        */
    }

    val firstItem: Item
        get() = entry.value()

    fun toStack(): ItemStack = ItemStack(entry, count, components)

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

    override fun toString(): String = "HTRecipeResult[count=$count, item=$entry, components=$components]"
}
