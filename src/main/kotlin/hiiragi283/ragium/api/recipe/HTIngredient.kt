package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.toList
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
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.registry.tag.TagKey
import java.util.function.Predicate

class HTIngredient private constructor(private val entryList: RegistryEntryList<Item>, val count: Int) : Predicate<ItemStack> {
    companion object {
        @JvmField
        val EMPTY = HTIngredient(RegistryEntryList.empty(), 0)

        @JvmField
        val CODEC: Codec<HTIngredient> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    RegistryCodecs
                        .entryList(RegistryKeys.ITEM, Registries.ITEM.codec)
                        .fieldOf("items")
                        .forGetter(HTIngredient::entryList),
                    Codec
                        .intRange(1, 99)
                        .optionalFieldOf("count", 1)
                        .forGetter(HTIngredient::count),
                ).apply(instance, ::HTIngredient)
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTIngredient> = PacketCodec.tuple(
            PacketCodecs.registryEntryList(RegistryKeys.ITEM),
            HTIngredient::entryList,
            PacketCodecs.INTEGER,
            HTIngredient::count,
            ::HTIngredient,
        )

        @JvmField
        val LIST_PACKET_CODEC: PacketCodec<RegistryByteBuf, List<HTIngredient>> = PACKET_CODEC.toList()

        @Suppress("DEPRECATION")
        @JvmStatic
        fun of(item: ItemConvertible, count: Int = 1): HTIngredient = when (val item1: Item = item.asItem()) {
            Items.AIR -> EMPTY
            else -> HTIngredient(RegistryEntryList.of(item1.registryEntry), count)
        }

        @JvmStatic
        fun of(tagKey: TagKey<Item>, count: Int = 1): HTIngredient = Registries.ITEM
            .getOrCreateEntryList(tagKey)
            .let { HTIngredient(it, count) }
        // .orElseThrow { IllegalArgumentException("$tagKey bounds no element!") }
    }

    val matchingStacks: List<ItemStack>
        get() = entryList.map { ItemStack(it, count) }

    override fun toString(): String = "HTIngredient[count=$count, ingredients=$entryList]"

    //    Predicate    //

    override fun test(stack: ItemStack): Boolean = when {
        stack.isEmpty -> this == EMPTY
        else -> entryList.any(stack::itemMatches)
    }
}
