package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.codec.RagiumCodecs
import hiiragi283.ragium.api.extension.*
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.MutableText
import net.minecraft.util.StringIdentifiable
import java.util.*
import java.util.function.Predicate

/**
 * アイテムと個数を扱う材料のクラス
 * @param entryList 条件に一致するアイテムの一覧
 * @param count 条件に一致するアイテムの個数
 * @param consumeType アイテムを減らす処理のタイプ
 */
class HTItemIngredient private constructor(
    private val entryList: RegistryEntryList<Item>,
    val count: Int = 1,
    val consumeType: ConsumeType = ConsumeType.DECREMENT,
) : Predicate<ItemStack> {
    companion object {
        @JvmStatic
        private val ITEM_CODEC: Codec<RegistryEntryList<Item>> = RegistryCodecs.entryList(RegistryKeys.ITEM)

        @JvmField
        val CODEC: Codec<HTItemIngredient> = RagiumCodecs.simpleOrComplex(
            ITEM_CODEC.xmap(::HTItemIngredient, HTItemIngredient::entryList),
            RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        ITEM_CODEC
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
            },
        ) { ingredient: HTItemIngredient -> ingredient.count == 1 && ingredient.consumeType == ConsumeType.DECREMENT }

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

        @JvmStatic
        fun of(entry: RegistryEntry<Item>, count: Int = 1, consumeType: ConsumeType = ConsumeType.DECREMENT): HTItemIngredient =
            HTItemIngredient(RegistryEntryList.of(entry), count, consumeType)

        @JvmStatic
        fun of(item: ItemConvertible, count: Int = 1, consumeType: ConsumeType = ConsumeType.DECREMENT): HTItemIngredient =
            HTItemIngredient(RegistryEntryList.of(item.registryEntry), count, consumeType)

        @JvmStatic
        fun of(items: List<ItemConvertible>, count: Int = 1, consumeType: ConsumeType = ConsumeType.DECREMENT): HTItemIngredient =
            HTItemIngredient(RegistryEntryList.of(ItemConvertible::registryEntry, items), count, consumeType)

        @JvmStatic
        fun of(tagKey: TagKey<Item>, count: Int = 1, consumeType: ConsumeType = ConsumeType.DECREMENT): HTItemIngredient =
            HTItemIngredient(Registries.ITEM.getOrCreateEntryList(tagKey), count, consumeType)
    }

    val isEmpty: Boolean
        get() = entryList.storage.map(
            { false },
            { it.isEmpty() || it.all { entry: RegistryEntry<Item> -> entry.value().isAir } },
        ) ||
            count <= 0

    fun <T : Any> map(transform: (Item, Int) -> T): List<T> = mapEntry { entry: RegistryEntry<Item>, count: Int ->
        transform(entry.value(), count)
    }

    fun <T : Any> mapEntry(transform: (RegistryEntry<Item>, Int) -> T): List<T> = entryList.map { transform(it, count) }

    val text: MutableText
        get() = entryList.asText(Item::getName)

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
            val CODEC: Codec<ConsumeType> = identifiedCodec(ConsumeType.entries)

            @JvmField
            val PACKET_CODEC: PacketCodec<RegistryByteBuf, ConsumeType> = identifiedPacketCodec(ConsumeType.entries)
        }

        override fun asString(): String = name.lowercase()
    }
}
