package hiiragi283.ragium.integration.jade

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.enchantment.HTEnchantmentHolder
import hiiragi283.ragium.integration.jade.base.HTBlockDataProvider
import net.minecraft.ChatFormatting
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.enchantment.ItemEnchantments
import snownee.jade.api.BlockAccessor
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

object HTEnchantmentHolderProvider : HTBlockDataProvider<ItemEnchantments>() {
    override fun getUid(): ResourceLocation = RagiumAPI.id("enchantable_block")

    override fun streamData(accessor: BlockAccessor): ItemEnchantments? {
        val enchHolder: HTEnchantmentHolder = accessor.blockEntity as? HTEnchantmentHolder ?: return null
        return enchHolder.exportEnch()
    }

    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, ItemEnchantments> = ItemEnchantments.STREAM_CODEC

    override fun appendTooltip(
        tooltip: ITooltip,
        accessor: BlockAccessor,
        config: IPluginConfig,
        data: ItemEnchantments,
    ) {
        data.addToTooltip(
            Item.TooltipContext.of(accessor.level),
            { text: Component ->
                tooltip.add(text.copy().withStyle(ChatFormatting.AQUA))
            },
            TooltipFlag.ADVANCED,
        )
    }
}
