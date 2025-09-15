package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.extension.lookupOrNull
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance
import java.util.Optional
import java.util.function.Function

@JvmRecord
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

    fun <T : Any> useInstance(
        function: Function<ResourceKey<Enchantment>, Optional<out Holder<Enchantment>>>,
        action: (Holder<Enchantment>, Int) -> T,
    ): Optional<T> = function.apply(key).map { holder: Holder<Enchantment> -> action(holder, level) }

    fun <T : Any> useInstance(provider: HolderLookup.Provider?, action: (Holder<Enchantment>, Int) -> T): Optional<T> =
        useInstance({ key: ResourceKey<Enchantment> ->
            provider?.lookupOrNull(Registries.ENCHANTMENT)?.get(key) ?: Optional.empty()
        }, action)

    fun getFullName(provider: HolderLookup.Provider?): Optional<Component> = useInstance(provider, Enchantment::getFullname)

    fun toInstance(provider: HolderLookup.Provider?): Optional<EnchantmentInstance> = useInstance(provider, ::EnchantmentInstance)

    fun toEnchBook(provider: HolderLookup.Provider?): Optional<ItemStack> =
        toInstance(provider).map(EnchantedBookItem::createForEnchantment)
}
