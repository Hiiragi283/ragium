package hiiragi283.ragium.integration.jade

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.extension.floatText
import hiiragi283.ragium.api.extension.intText
import hiiragi283.ragium.api.util.RagiumTranslationKeys
import net.minecraft.ChatFormatting
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import snownee.jade.api.BlockAccessor
import snownee.jade.api.IComponentProvider
import snownee.jade.api.IServerDataProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

object HTMachineInfoProvider : IServerDataProvider<BlockAccessor>, IComponentProvider<BlockAccessor> {
    @JvmField
    val TICK_RATE: MapCodec<Int> = Codec.INT.fieldOf("tick_rate")

    @JvmField
    val COST_MODIFIER: MapCodec<Int> = Codec.INT.fieldOf("cost_modifier")

    override fun appendServerData(tag: CompoundTag, accessor: BlockAccessor) {
        val machineEntity: HTMachineBlockEntity = accessor.blockEntity as? HTMachineBlockEntity ?: return
        accessor.writeData(TICK_RATE, machineEntity.tickRate)
        accessor.writeData(COST_MODIFIER, machineEntity.costModifier)
    }

    override fun getUid(): ResourceLocation = RagiumAPI.id("machine_info")

    //    IComponentProvider    //

    override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
        val tickRate: Int = accessor.readData(TICK_RATE).orElse(200)
        tooltip.add(
            Component.translatable(
                RagiumTranslationKeys.MACHINE_TICK_RATE,
                intText(tickRate).withStyle(ChatFormatting.WHITE),
                floatText(tickRate / 20f).withStyle(ChatFormatting.WHITE),
            ),
        )

        val costModifier: Int = accessor.readData(COST_MODIFIER).orElse(1)
        tooltip.add(
            Component.translatable(
                RagiumTranslationKeys.MACHINE_COST_MODIFIER,
                intText(costModifier).withStyle(ChatFormatting.WHITE),
            ),
        )
    }
}
