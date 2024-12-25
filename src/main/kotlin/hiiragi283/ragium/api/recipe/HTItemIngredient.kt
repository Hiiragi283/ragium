package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.identifiedCodec
import hiiragi283.ragium.api.extension.identifiedPacketCodec
import hiiragi283.ragium.api.extension.isAir
import hiiragi283.ragium.api.extension.isIn
import hiiragi283.ragium.api.recipe.HTItemIngredient.ConsumeType.entries
import hiiragi283.ragium.api.util.HTRegistryEntryList
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.MutableText
import net.minecraft.util.StringIdentifiable
import java.util.*
import java.util.function.Predicate

class HTItemIngredient private constructor(
    private val entryList: HTRegistryEntryList<Item>,
    val count: Int,
    val consumeType: ConsumeType,
) : Predicate<ItemStack> {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemIngredient> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTRegistryEntryList
                        .codec(Registries.ITEM)
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
                HTRegistryEntryList.packetCodec(Registries.ITEM),
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
            HTItemIngredient(HTRegistryEntryList.of(item.asItem()), count, consumeType)

        @JvmStatic
        fun of(tagKey: TagKey<Item>, count: Int = 1, consumeType: ConsumeType = ConsumeType.DECREMENT): HTItemIngredient =
            HTItemIngredient(HTRegistryEntryList.ofTag(tagKey, Registries.ITEM), count, consumeType)
    }

    val isEmpty: Boolean
        get() = entryList.storage.map({ false }, { it.isAir }) || count <= 0

    fun <T : Any> map(transform: (Item, Int) -> T): List<T> = entryList.map { transform(it, count) }

    val text: MutableText
        get() = entryList.getText(Item::getName)

    val entryMap: Map<Item, Int> = entryList.associateWith { count }

    val matchingStacks: List<ItemStack>
        get() = entryMap.map { (item: Item, count: Int) -> ItemStack(item, count) }

    val vanillaIngredient: Ingredient
        get() = entryList.storage.map(Ingredient::fromTag, Ingredient::ofItems)

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

    override fun equals(other: Any?): Boolean = (other as? HTItemIngredient)
        ?.let { it.entryList == this.entryList && it.count == this.count } == true

    override fun hashCode(): Int = Objects.hash(entryList, count)

    override fun toString(): String = "HTItemIngredient[entryList=${text.string},count=$count]"

    //    ConsumeType    //

    enum class ConsumeType : StringIdentifiable {
        DECREMENT,
        DAMAGE,
        ;

        companion object {
            @JvmField
            val CODEC: Codec<ConsumeType> = identifiedCodec(entries)

            @JvmField
            val PACKET_CODEC: PacketCodec<RegistryByteBuf, ConsumeType> = identifiedPacketCodec(entries)
        }

        override fun asString(): String = name.lowercase()
    }
}
