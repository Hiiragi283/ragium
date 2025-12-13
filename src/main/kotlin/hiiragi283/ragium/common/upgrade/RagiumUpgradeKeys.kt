package hiiragi283.ragium.common.upgrade

import hiiragi283.ragium.api.upgrade.HTUpgradeKey

object RagiumUpgradeKeys {
    //    Processor    //

    @JvmField
    val COMPOSTING: HTUpgradeKey = HTUpgradeKey.get("composting")

    @JvmField
    val DISABLE_EXTRA: HTUpgradeKey = HTUpgradeKey.get("disable_extra")

    @JvmField
    val EXP_DRAIN: HTUpgradeKey = HTUpgradeKey.get("exp_drain")

    @JvmField
    val LONG_BREWING: HTUpgradeKey = HTUpgradeKey.get("long_brewing")

    @JvmField
    val STRONG_BREWING: HTUpgradeKey = HTUpgradeKey.get("strong_brewing")

    @JvmField
    val USE_LUBRICANT: HTUpgradeKey = HTUpgradeKey.get("use_lubricant")

    //    Device    //

    @JvmField
    val EXP_COLLECTING: HTUpgradeKey = HTUpgradeKey.get("exp_collecting")

    @JvmField
    val FISHING: HTUpgradeKey = HTUpgradeKey.get("fishing")

    @JvmField
    val MOB_CAPTURE: HTUpgradeKey = HTUpgradeKey.get("mob_capture")
}
