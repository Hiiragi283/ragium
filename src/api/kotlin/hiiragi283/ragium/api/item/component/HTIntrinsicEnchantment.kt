package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance
import java.util.Optional

data class HTIntrinsicEnchantment(val key: ResourceKey<Enchantment>, val level: Int) {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTIntrinsicEnchantment> = BiCodec.composite(
            BiCodecs.resourceKey(Registries.ENCHANTMENT).fieldOf("enchantment"),
            HTIntrinsicEnchantment::key,
            BiCodec.INT.optionalFieldOf("level", 1),
            HTIntrinsicEnchantment::level,
            ::HTIntrinsicEnchantment,
        )
    }

    fun getInstance(lookup: HolderGetter<Enchantment>): Optional<EnchantmentInstance> =
        lookup.get(key).map { holder: Holder.Reference<Enchantment> -> EnchantmentInstance(holder, level) }
}
