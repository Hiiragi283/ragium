package hiiragi283.ragium.api.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * 確率付きの[net.minecraft.world.item.ItemStack]を表すクラス
 */
data class HTChancedItemStack(val item: Holder<Item>, val count: Int, val chance: Float) {
    companion object {
        @JvmField
        val CODEC: Codec<HTChancedItemStack> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    ItemStack.ITEM_NON_AIR_CODEC.fieldOf("item").forGetter(HTChancedItemStack::item),
                    Codec.intRange(1, 99).optionalFieldOf("count", 1).forGetter(HTChancedItemStack::count),
                    Codec.floatRange(0f, 1f).fieldOf("chance").forGetter(HTChancedItemStack::chance),
                ).apply(instance, ::HTChancedItemStack)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTChancedItemStack> = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Registries.ITEM),
            HTChancedItemStack::item,
            ByteBufCodecs.INT,
            HTChancedItemStack::count,
            ByteBufCodecs.FLOAT,
            HTChancedItemStack::chance,
            ::HTChancedItemStack,
        )
    }

    fun toStack(): ItemStack = ItemStack(item, count)
}
