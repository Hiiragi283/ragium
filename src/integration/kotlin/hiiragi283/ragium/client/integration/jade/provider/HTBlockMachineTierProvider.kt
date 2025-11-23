package hiiragi283.ragium.client.integration.jade.provider

import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.client.integration.jade.provider.base.HTBasicJadeDataProvider
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.common.text.RagiumCommonTranslation
import snownee.jade.api.BlockAccessor
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

object HTBlockMachineTierProvider : HTBasicJadeDataProvider<BlockAccessor, HTBaseTier>(
    "block_machine_tier",
    HTBaseTier.CODEC.streamCodec.cast(),
) {
    override fun streamData(accessor: BlockAccessor): HTBaseTier? = (accessor.blockEntity as? HTMachineBlockEntity)?.getMaxMachineTier()

    override fun appendTooltip(
        tooltip: ITooltip,
        accessor: BlockAccessor,
        config: IPluginConfig,
        data: HTBaseTier,
    ) {
        tooltip.add(RagiumCommonTranslation.JADE_MACHINE_TIER.translate(data.color, data))
    }
}
