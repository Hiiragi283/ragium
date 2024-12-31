package hiiragi283.ragium.api.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.entryPacketCodec
import net.minecraft.component.ComponentChanges
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry

/**
 * アイテムの完成品を扱うクラス
 * @param entry アイテムの[RegistryEntry]
 * @param count 完成品の個数
 * @param components 完成品のコンポーネント
 */
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

    @Suppress("DEPRECATION")
    constructor(item: ItemConvertible, count: Int = 1, components: ComponentChanges = ComponentChanges.EMPTY) : this(
        item.asItem().registryEntry,
        count,
        components,
    )

    /**
     * 指定した[stack]から[HTItemResult]を返します。
     */
    constructor(stack: ItemStack) : this(stack.registryEntry, stack.count, stack.componentChanges)

    val item: Item
        get() = entry.value()
    val stack: ItemStack
        get() = ItemStack(entry, count, components)

    /**
     * 指定した[other]にマージできるか判定します。
     */
    fun canMerge(other: ItemStack): Boolean = when {
        other.isEmpty -> true
        other.count + this.count > other.maxCount -> false
        ItemStack.areItemsAndComponentsEqual(stack, other) -> true
        else -> false
    }

    /**
     * 指定した[other]にマージします。
     * @return [other]とマージした[ItemStack]
     */
    fun merge(other: ItemStack): ItemStack = when {
        other.isEmpty -> stack
        other.count + this.count > other.maxCount -> other
        ItemStack.areItemsAndComponentsEqual(stack, other) -> other.apply { count += stack.count }
        else -> other
    }

    override fun toString(): String = "HTItemResult[item=${entry.idAsString},count=$count,components=$components]"
}
