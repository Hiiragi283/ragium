package hiiragi283.ragium.client.integration.jade.provider

import hiiragi283.ragium.api.block.entity.HTBlockEntityWithUpgrade
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
    override fun streamData(accessor: BlockAccessor): HTMachineUpgrade? = (accessor.blockEntity as? HTMachineBlockEntity)
        ?.let(HTBlockEntityWithUpgrade::collectAllModifier)
        ?.let(HTMachineUpgrade::create)

    override fun appendTooltip(
        tooltip: ITooltip,
        accessor: BlockAccessor,
        config: IPluginConfig,
        data: HTMachineUpgrade,
    ) {
        data.addToTooltip(tooltip::add)
    }
}
