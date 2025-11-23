package hiiragi283.ragium.client.integration.jade.provider

import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.client.integration.jade.provider.base.HTBasicJadeDataProvider
import hiiragi283.ragium.common.block.entity.HTMachineBlockEntity
import snownee.jade.api.BlockAccessor
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

object HTBlockMachinePropertiesProvider : HTBasicJadeDataProvider<BlockAccessor, HTMachineUpgrade>(
    "block_machine_properties",
    HTMachineUpgrade.CODEC.streamCodec.cast(),
) {
    override fun streamData(accessor: BlockAccessor): HTMachineUpgrade? {
        val machine: HTMachineBlockEntity = accessor.blockEntity as? HTMachineBlockEntity ?: return null
        return HTMachineUpgrade(HTMachineUpgrade.Key.entries.associateWith(machine::collectModifier))
    }

    override fun appendTooltip(
        tooltip: ITooltip,
        accessor: BlockAccessor,
        config: IPluginConfig,
        data: HTMachineUpgrade,
    ) {
        data.addToTooltip(tooltip::add)
    }
}
