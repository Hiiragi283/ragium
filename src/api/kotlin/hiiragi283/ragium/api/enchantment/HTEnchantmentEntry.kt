package hiiragi283.ragium.api.enchantment

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance

/**
 * [Enchantment]の[Holder]とそのレベルを束ねたデータクラス
 * @see EnchantmentInstance
 */
data class HTEnchantmentEntry(val holder: Holder<Enchantment>, val level: Int) {
    companion object {
        @JvmField
        val CODEC: Codec<HTEnchantmentEntry> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    Enchantment.CODEC.fieldOf("enchantment").forGetter(HTEnchantmentEntry::holder),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("level", 0).forGetter(HTEnchantmentEntry::level),
                ).apply(instance, ::HTEnchantmentEntry)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTEnchantmentEntry> = StreamCodec.composite(
            Enchantment.STREAM_CODEC,
            HTEnchantmentEntry::holder,
            ByteBufCodecs.VAR_INT,
            HTEnchantmentEntry::level,
            ::HTEnchantmentEntry,
        )
    }

    constructor(entry: Map.Entry<Holder<Enchantment>, Int>) : this(entry.key, entry.value)
}
