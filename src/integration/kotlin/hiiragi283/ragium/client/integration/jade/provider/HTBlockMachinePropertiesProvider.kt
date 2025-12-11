package hiiragi283.ragium.client.integration.jade.provider

import hiiragi283.ragium.api.item.component.HTComponentUpgrade
import hiiragi283.ragium.api.upgrade.HTUpgradeHelper
import hiiragi283.ragium.api.upgrade.HTUpgradeKey
import hiiragi283.ragium.client.integration.jade.provider.base.HTBasicJadeDataProvider
import hiiragi283.ragium.common.block.entity.HTUpgradableBlockEntity
import hiiragi283.ragium.common.block.entity.component.HTMachineUpgradeComponent
import org.apache.commons.lang3.math.Fraction
import snownee.jade.api.BlockAccessor
import snownee.jade.api.ITooltip
import snownee.jade.api.config.IPluginConfig

object HTBlockMachinePropertiesProvider : HTBasicJadeDataProvider<BlockAccessor, HTComponentUpgrade>(
    "block_machine_properties",
    HTComponentUpgrade.CODEC.streamCodec.cast(),
) {
    override fun streamData(accessor: BlockAccessor): HTComponentUpgrade? = (accessor.blockEntity as? HTUpgradableBlockEntity)
        ?.machineUpgrade
        ?.let { component: HTMachineUpgradeComponent -> HTUpgradeKey.getAll().associateWith(component::collectMultiplier) }
        ?.let { map: Map<HTUpgradeKey, Fraction> -> map.filter { (_, value: Fraction) -> value > Fraction.ZERO } }
        ?.let(HTComponentUpgrade.Companion::create)

    override fun appendTooltip(
        tooltip: ITooltip,
        accessor: BlockAccessor,
        config: IPluginConfig,
        data: HTComponentUpgrade,
    ) {
        HTUpgradeHelper.appendTooltips(data, tooltip::add)
    }
}
