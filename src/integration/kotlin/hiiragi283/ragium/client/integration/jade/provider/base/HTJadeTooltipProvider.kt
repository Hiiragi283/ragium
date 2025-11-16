package hiiragi283.ragium.client.integration.jade.provider.base

import snownee.jade.api.Accessor
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

fun interface HTJadeTooltipProvider<ACCESSOR : Accessor<*>, DATA : Any> {
    fun appendTooltip(
        tooltip: ITooltip,
        accessor: ACCESSOR,
        config: IPluginConfig,
        data: DATA,
    )
}
