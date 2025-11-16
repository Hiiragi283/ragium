package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.text.HTTextResult
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
import net.minecraft.world.item.component.TooltipProvider
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance
import java.util.function.Consumer
import java.util.function.Function

@JvmRecord
data class HTIntrinsicEnchantment(val entry: HTKeyOrTagEntry<Enchantment>, val level: Int) : TooltipProvider {
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

    fun <T : Any> useInstance(getter: HolderGetter<Enchantment>, action: (Holder<Enchantment>, Int) -> T): HTTextResult<T> =
        entry.getFirstHolder(getter).map { holder: Holder<Enchantment> -> action(holder, level) }

    fun <T : Any> useInstance(provider: HolderLookup.Provider?, action: (Holder<Enchantment>, Int) -> T): HTTextResult<T> =
        entry.getFirstHolder(provider).map { holder: Holder<Enchantment> -> action(holder, level) }

    fun getFullName(provider: HolderLookup.Provider?): HTTextResult<Component> = useInstance(provider, Enchantment::getFullname)

    fun toInstance(provider: HolderLookup.Provider?): HTTextResult<EnchantmentInstance> = useInstance(provider, ::EnchantmentInstance)

    fun toEnchBook(provider: HolderLookup.Provider?): HTTextResult<ItemStack> =
        toInstance(provider).map(EnchantedBookItem::createForEnchantment)

    override fun addToTooltip(context: Item.TooltipContext, tooltipAdder: Consumer<Component>, tooltipFlag: TooltipFlag) {
        when {
            tooltipFlag.hasShiftDown() -> getFullName(context.registries())
                .fold(
                    { RagiumTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT.translate(it) },
                    Function.identity<Component>()::apply,
                )
            else -> RagiumTranslation.TOOLTIP_SHOW_DESCRIPTION.translateColored(ChatFormatting.YELLOW)
        }.let(tooltipAdder::accept)
    }
}
