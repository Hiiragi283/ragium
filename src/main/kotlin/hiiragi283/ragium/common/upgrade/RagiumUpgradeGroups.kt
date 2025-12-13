package hiiragi283.ragium.common.upgrade

import hiiragi283.ragium.api.upgrade.HTUpgradeGroup

object RagiumUpgradeGroups {
    //    Processor    //

    //    Device    //

    @JvmField
    val FLUID_COLLECTOR: HTUpgradeGroup = HTUpgradeGroup.get("fluid_collector")

    @JvmField
    val ITEM_COLLECTOR: HTUpgradeGroup = HTUpgradeGroup.get("item_collector")
}
