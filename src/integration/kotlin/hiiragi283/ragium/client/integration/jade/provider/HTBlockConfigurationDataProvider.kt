package hiiragi283.ragium.client.integration.jade.provider

import hiiragi283.ragium.api.util.access.HTAccessConfigGetter
import hiiragi283.ragium.api.util.access.HTAccessConfiguration
import snownee.jade.api.BlockAccessor
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

object HTBlockConfigurationDataProvider : HTBlockDataProvider<HTAccessConfiguration>(
    "block_configuration",
    HTAccessConfiguration.CODEC.cast(),
) {
    override fun streamData(accessor: BlockAccessor): HTAccessConfiguration? =
        (accessor.blockEntity as? HTAccessConfigGetter)?.getAccessConfig(accessor.side)

    override fun appendTooltip(
        tooltip: ITooltip,
        accessor: BlockAccessor,
        config: IPluginConfig,
        data: HTAccessConfiguration,
    ) {
        tooltip.add(data.getText())
    }
}
