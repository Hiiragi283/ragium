package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.extension.getOrNull
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance

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

    fun <T> useInstance(lookup: HolderGetter<Enchantment>?, action: (Holder<Enchantment>, Int) -> T): T? =
        lookup?.getOrNull(key)?.let { holder: Holder<Enchantment> -> action(holder, level) }

    fun getFullName(lookup: HolderGetter<Enchantment>?): Component? = useInstance(lookup, Enchantment::getFullname)

    fun toInstance(lookup: HolderGetter<Enchantment>?): EnchantmentInstance? = useInstance(lookup, ::EnchantmentInstance)

    fun toEnchBook(lookup: HolderGetter<Enchantment>?): ItemStack? = toInstance(lookup)?.let(EnchantedBookItem::createForEnchantment)
}
