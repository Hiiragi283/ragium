package hiiragi283.ragium.client.integration.jade.provider

import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.api.util.access.HTAccessConfigGetter
import snownee.jade.api.BlockAccessor
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

object HTBlockConfigurationDataProvider : HTBlockDataProvider<HTAccessConfig>(
    "block_configuration",
    HTAccessConfig.CODEC.cast(),
) {
    override fun streamData(accessor: BlockAccessor): HTAccessConfig? =
        (accessor.blockEntity as? HTAccessConfigGetter)?.getAccessConfig(accessor.side)

    override fun appendTooltip(
        tooltip: ITooltip,
        accessor: BlockAccessor,
        config: IPluginConfig,
        data: HTAccessConfig,
    ) {
        tooltip.add(data.getText())
    }
}
