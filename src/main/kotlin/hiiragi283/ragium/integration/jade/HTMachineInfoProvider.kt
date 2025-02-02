package hiiragi283.ragium.integration.jade

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.boolText
import hiiragi283.ragium.api.extension.floatText
import hiiragi283.ragium.api.extension.intText
import hiiragi283.ragium.api.machine.HTMachineAccess
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.enchantment.ItemEnchantments
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IComponentProvider
import snownee.jade.api.IServerDataProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig
import kotlin.jvm.optionals.getOrNull

object HTMachineInfoProvider : IServerDataProvider<BlockAccessor>, IComponentProvider<BlockAccessor> {
    @JvmField
    val IS_ACTIVE: MapCodec<Boolean> = Codec.BOOL.fieldOf("is_active")

    @JvmField
    val TICK_RATE: MapCodec<Int> = Codec.INT.fieldOf("tick_rate")

    @JvmField
    val SHOW_PREVIEW: MapCodec<Boolean> = Codec.BOOL.fieldOf("show_preview")

    @JvmField
    val ENCHANTMENT: MapCodec<ItemEnchantments> = ItemEnchantments.CODEC.fieldOf("enchantments")

    override fun appendServerData(tag: CompoundTag, accessor: BlockAccessor) {
        val machineEntity: HTMachineAccess = accessor.blockEntity as? HTMachineAccess ?: return
        accessor.writeData(HTMachineKey.FIELD_CODEC, machineEntity.machineKey)
        accessor.writeData(TICK_RATE, machineEntity.tickRate)
        accessor.writeData(IS_ACTIVE, machineEntity.isActive)
        accessor.writeData(SHOW_PREVIEW, machineEntity.showPreview)
        accessor.writeData(ENCHANTMENT, machineEntity.enchantments)
    }

    override fun getUid(): ResourceLocation? = RagiumAPI.id("machine_info")

    //    IComponentProvider    //

    override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
        val machineKey: HTMachineKey = accessor.readData(HTMachineKey.FIELD_CODEC).getOrNull() ?: return
        machineKey.appendTooltip(tooltip::add, false)

        val isActive: Boolean = accessor.readData(IS_ACTIVE).orElse(false)
        tooltip.add(Component.translatable(RagiumTranslationKeys.MACHINE_WORKING, boolText(isActive)))

        val tickRate: Int = accessor.readData(TICK_RATE).orElse(200)
        tooltip.add(
            Component.translatable(
                RagiumTranslationKeys.MACHINE_TICK_RATE,
                intText(tickRate),
                floatText(tickRate / 20f),
            ),
        )

        val showPreview: Boolean = accessor.readData(SHOW_PREVIEW).orElse(false)
        tooltip.add(Component.translatable(RagiumTranslationKeys.MACHINE_PREVIEW, boolText(showPreview)))

        val enchantments: ItemEnchantments = accessor.readData(ENCHANTMENT).orElse(ItemEnchantments.EMPTY)
        enchantments.addToTooltip(Item.TooltipContext.of(accessor.level), tooltip::add, TooltipFlag.ADVANCED)
    }
}
