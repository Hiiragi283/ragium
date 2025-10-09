package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.item.HTTooltipProvider
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import hiiragi283.ragium.api.text.RagiumTranslation
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance

@JvmRecord
data class HTIntrinsicEnchantment(val entry: HTKeyOrTagEntry<Enchantment>, val level: Int) : HTTooltipProvider {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTIntrinsicEnchantment> = BiCodec.composite(
            HTKeyOrTagHelper.INSTANCE.codec(Registries.ENCHANTMENT).fieldOf("enchantment"),
            HTIntrinsicEnchantment::entry,
            BiCodec.INT.optionalFieldOf("level", 1),
            HTIntrinsicEnchantment::level,
            ::HTIntrinsicEnchantment,
        )
    }

    constructor(key: ResourceKey<Enchantment>, level: Int) : this(HTKeyOrTagHelper.INSTANCE.create(key), level)

    fun <T : Any> useInstance(getter: HolderGetter<Enchantment>, action: (Holder<Enchantment>, Int) -> T): Result<T> =
        entry.getFirstHolder(getter).map { holder: Holder<Enchantment> -> action(holder, level) }

    fun <T : Any> useInstance(provider: HolderLookup.Provider?, action: (Holder<Enchantment>, Int) -> T): Result<T> =
        entry.getFirstHolder(provider).map { holder: Holder<Enchantment> -> action(holder, level) }

    fun getFullName(provider: HolderLookup.Provider?): Result<Component> = useInstance(provider, Enchantment::getFullname)

    fun toInstance(provider: HolderLookup.Provider?): Result<EnchantmentInstance> = useInstance(provider, ::EnchantmentInstance)

    fun toEnchBook(provider: HolderLookup.Provider?): Result<ItemStack> = toInstance(provider).map(EnchantedBookItem::createForEnchantment)

    override fun addToTooltip(context: Item.TooltipContext, consumer: (Component) -> Unit, flag: TooltipFlag) {
        getFullName(context.registries()).onSuccess { text: Component ->
            when {
                flag.hasShiftDown() -> RagiumTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT.getComponent(text)
                else -> RagiumTranslation.TOOLTIP_SHOW_INFO.getColoredComponent(ChatFormatting.YELLOW)
            }.let(consumer)
        }
    }
}
