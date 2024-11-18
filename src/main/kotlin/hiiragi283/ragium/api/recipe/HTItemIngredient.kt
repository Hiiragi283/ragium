package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.asText
import hiiragi283.ragium.api.extension.codecOf
import hiiragi283.ragium.api.extension.isOf
import hiiragi283.ragium.api.extension.packetCodecOf
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
import net.minecraft.util.StringIdentifiable
import java.util.function.Predicate

class HTItemIngredient private constructor(private val entryList: RegistryEntryList<Item>, val count: Int, val consumeType: ConsumeType) :
    Predicate<ItemStack> {
        companion object {
            @JvmField
            val CODEC: Codec<HTItemIngredient> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        RegistryCodecs
                            .entryList(RegistryKeys.ITEM)
                            .fieldOf("items")
                            .forGetter(HTItemIngredient::entryList),
                        Codec
                            .intRange(1, Int.MAX_VALUE)
                            .optionalFieldOf("count", 1)
                            .forGetter(HTItemIngredient::count),
                        ConsumeType.CODEC
                            .optionalFieldOf("consume_type", ConsumeType.DECREMENT)
                            .forGetter(HTItemIngredient::consumeType),
                    ).apply(instance, ::HTItemIngredient)
            }

            @JvmField
            val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTItemIngredient> = PacketCodec
                .tuple(
                    PacketCodecs.registryEntryList(RegistryKeys.ITEM),
                    HTItemIngredient::entryList,
                    PacketCodecs.VAR_INT,
                    HTItemIngredient::count,
                    ConsumeType.PACKET_CODEC,
                    HTItemIngredient::consumeType,
                    ::HTItemIngredient,
                )

            @Suppress("DEPRECATION")
            @JvmStatic
            fun of(item: ItemConvertible, count: Int = 1, consumeType: ConsumeType = ConsumeType.DECREMENT): HTItemIngredient =
                HTItemIngredient(RegistryEntryList.of(item.asItem().registryEntry), count, consumeType)

            @Suppress("DEPRECATION")
            @JvmStatic
            fun of(tagKey: TagKey<Item>, count: Int = 1, consumeType: ConsumeType = ConsumeType.DECREMENT): HTItemIngredient =
                HTItemIngredient(Registries.ITEM.getOrCreateEntryList(tagKey), count, consumeType)
        }

        val isEmpty: Boolean
            get() = entryList.storage.map(
                { false },
                { it.isEmpty() || it.any { entry: RegistryEntry<Item> -> entry.isOf(Items.AIR) } },
            ) ||
                count <= 0

        fun <T : Any> map(transform: (RegistryEntry<Item>, Int) -> T): List<T> = entryList.map { transform(it, count) }

        val firstEntry: RegistryEntry<Item>?
            get() = entryList.firstOrNull()

        val entryMap: Map<RegistryEntry<Item>, Int> = entryList.associateWith { count }

        val valueMap: Map<Item, Int>
            get() = entryMap.mapKeys { it.key.value() }

        override fun test(stack: ItemStack): Boolean = when (stack.isEmpty) {
            true -> this.isEmpty
            false -> stack.isIn(entryList) && stack.count >= count
        }

        fun onConsume(stack: ItemStack) {
            when (consumeType) {
                ConsumeType.DECREMENT -> stack.decrement(count)
                ConsumeType.DAMAGE -> {
                    val damage: Int = stack.damage
                    val maxDamage: Int = stack.maxDamage
                    if (damage + 1 <= maxDamage) {
                        stack.damage += count
                    } else {
                        stack.decrement(1)
                    }
                }
            }
        }

        override fun toString(): String = "HTItemIngredient[entryList=${entryList.asText(Item::getName)},count=$count]"

        //    ConsumeType    //

        enum class ConsumeType : StringIdentifiable {
            DECREMENT,
            DAMAGE,
            ;

            companion object {
                @JvmField
                val CODEC: Codec<ConsumeType> = codecOf(entries)

                @JvmField
                val PACKET_CODEC: PacketCodec<RegistryByteBuf, ConsumeType> = packetCodecOf(entries)
            }

            override fun asString(): String = name.lowercase()
        }
    }
