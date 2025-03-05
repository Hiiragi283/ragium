package hiiragi283.ragium.integration.jade

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTEnchantableBlockEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.enchantment.ItemEnchantments
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IComponentProvider
import snownee.jade.api.IServerDataProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

object HTEnchantableBlockProvider : IServerDataProvider<BlockAccessor>, IComponentProvider<BlockAccessor> {
    @JvmField
    val CODEC: MapCodec<ItemEnchantments> = ItemEnchantments.CODEC.fieldOf("enchantments")

    override fun appendServerData(tag: CompoundTag, accessor: BlockAccessor) {
        val blockEntity: HTEnchantableBlockEntity = accessor.blockEntity as? HTEnchantableBlockEntity ?: return
        val enchantments: ItemEnchantments = blockEntity.enchantments
        accessor.writeData(CODEC, enchantments)
    }

    override fun getUid(): ResourceLocation = RagiumAPI.id("enchantable_block")

    override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
        accessor.readData(CODEC).ifPresent { enchantments: ItemEnchantments ->
            enchantments.addToTooltip(Item.TooltipContext.of(accessor.level), tooltip::add, TooltipFlag.ADVANCED)
        }
    }
}
