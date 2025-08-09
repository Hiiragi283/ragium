package hiiragi283.ragium.api.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance
import java.util.Optional

data class HTIntrinsicEnchantment(val key: ResourceKey<Enchantment>, val level: Int) {
    companion object {
        @JvmField
        val CODEC: Codec<HTIntrinsicEnchantment> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    ResourceKey.codec(Registries.ENCHANTMENT).fieldOf("enchantment").forGetter(HTIntrinsicEnchantment::key),
                    ExtraCodecs.POSITIVE_INT.optionalFieldOf("level", 1).forGetter(HTIntrinsicEnchantment::level),
                ).apply(instance, ::HTIntrinsicEnchantment)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTIntrinsicEnchantment> = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.ENCHANTMENT),
            HTIntrinsicEnchantment::key,
            ByteBufCodecs.VAR_INT,
            HTIntrinsicEnchantment::level,
            ::HTIntrinsicEnchantment,
        )
    }

    fun getInstance(lookup: HolderGetter<Enchantment>): Optional<EnchantmentInstance> =
        lookup.get(key).map { holder: Holder.Reference<Enchantment> -> EnchantmentInstance(holder, level) }
}
