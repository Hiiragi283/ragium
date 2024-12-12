package hiiragi283.ragium.api.storage

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.NON_NEGATIVE_LONG_CODEC
import hiiragi283.ragium.api.extension.isIn
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.impl.transfer.VariantCodecs
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.tag.TagKey

@Suppress("UnstableApiUsage")
class HTItemVariantStack(override val variant: ItemVariant, override val amount: Long) : HTVariantStack<Item, ItemVariant> {
    companion object {
        @JvmField
        val EMPTY = HTItemVariantStack(ItemVariant.blank(), 0)

        @JvmField
        val CODEC: Codec<HTItemVariantStack> = RecordCodecBuilder
            .create<HTItemVariantStack> { instance ->
                instance
                    .group(
                        VariantCodecs.ITEM_CODEC.fieldOf("variant").forGetter(HTItemVariantStack::variant),
                        NON_NEGATIVE_LONG_CODEC.fieldOf("amount").forGetter(HTItemVariantStack::amount),
                    ).apply(instance, ::HTItemVariantStack)
            }.validate(HTVariantStack.Companion::validate)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTItemVariantStack> = PacketCodec.tuple(
            VariantCodecs.ITEM_PACKET_CODEC,
            HTItemVariantStack::variant,
            PacketCodecs.VAR_LONG,
            HTItemVariantStack::amount,
            ::HTItemVariantStack,
        )
    }
    constructor(item: ItemConvertible, amount: Long) : this(ItemVariant.of(item), amount)

    constructor(stack: ItemStack) : this(ItemVariant.of(stack), stack.count.toLong())

    val item: Item = variant.item

    fun isIn(tagKey: TagKey<Item>): Boolean = variant.isIn(tagKey)
}
