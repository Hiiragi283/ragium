package hiiragi283.ragium.integration.jade.base

import snownee.jade.api.BlockAccessor
import snownee.jade.api.IBlockComponentProvider
import snownee.jade.api.ITooltip
import snownee.jade.api.StreamServerDataProvider
import snownee.jade.api.config.IPluginConfig

abstract class HTBlockDataProvider<T : Any> :
    StreamServerDataProvider<BlockAccessor, T>,
    IBlockComponentProvider {
    final override fun appendTooltip(tooltip: ITooltip, accessor: BlockAccessor, config: IPluginConfig) {
        decodeFromData(accessor).ifPresent { data: T ->
            appendTooltip(tooltip, accessor, config, data)
        }
    }

    abstract fun appendTooltip(
        tooltip: ITooltip,
        accessor: BlockAccessor,
        config: IPluginConfig,
        data: T,
    )
}
