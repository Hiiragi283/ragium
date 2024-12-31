package hiiragi283.ragium.api.storage

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.NON_NEGATIVE_LONG_CODEC
import hiiragi283.ragium.api.extension.isIn
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.tag.TagKey

/**
 * [ItemVariant]向けの[HTVariantStack]の実装
 */
class HTItemVariantStack(override val variant: ItemVariant, override val amount: Long) : HTVariantStack<Item, ItemVariant> {
    companion object {
        /**
         * [HTVariantStack.isEmpty]が常にtrueとなるインスタンス
         */
        @JvmField
        val EMPTY = HTItemVariantStack(ItemVariant.blank(), 0)

        @JvmField
        val CODEC: Codec<HTItemVariantStack> = RecordCodecBuilder
            .create<HTItemVariantStack> { instance ->
                instance
                    .group(
                        ItemVariant.CODEC.fieldOf("variant").forGetter(HTItemVariantStack::variant),
                        NON_NEGATIVE_LONG_CODEC.fieldOf("amount").forGetter(HTItemVariantStack::amount),
                    ).apply(instance, ::HTItemVariantStack)
            }.validate(HTVariantStack.Companion::validate)

        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTItemVariantStack> = PacketCodec.tuple(
            ItemVariant.PACKET_CODEC,
            HTItemVariantStack::variant,
            PacketCodecs.VAR_LONG,
            HTItemVariantStack::amount,
            ::HTItemVariantStack,
        )
    }
    constructor(item: ItemConvertible, amount: Long) : this(ItemVariant.of(item), amount)

    constructor(stack: ItemStack) : this(ItemVariant.of(stack), stack.count.toLong())

    /**
     * [ItemVariant]のオブジェクト
     */
    val item: Item = variant.item

    /**
     * 指定した[tagKey]に[variant]が含まれているか判定します。
     * @see [ItemVariant.isIn]
     */
    fun isIn(tagKey: TagKey<Item>): Boolean = variant.isIn(tagKey)
}
