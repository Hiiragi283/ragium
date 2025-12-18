package hiiragi283.ragium.common.upgrade

import hiiragi283.ragium.api.upgrade.HTUpgradeKey

object RagiumUpgradeKeys {
    //    Processor    //

    @JvmField
    val BLASTING: HTUpgradeKey = HTUpgradeKey.get("blasting")

    @JvmField
    val SMOKING: HTUpgradeKey = HTUpgradeKey.get("smoking")

    @JvmField
    val COMPOST_BIO: HTUpgradeKey = HTUpgradeKey.get("compost_bio")

    @JvmField
    val VOID_EXTRA: HTUpgradeKey = HTUpgradeKey.get("void_extra")

    @JvmField
    val EXTRACT_EXPERIENCE: HTUpgradeKey = HTUpgradeKey.get("extract_experience")

    @JvmField
    val USE_LUBRICANT: HTUpgradeKey = HTUpgradeKey.get("use_lubricant")

    //    Device    //

    @JvmField
    val COLLECT_EXP: HTUpgradeKey = HTUpgradeKey.get("collect_exp")

    @JvmField
    val FISHING: HTUpgradeKey = HTUpgradeKey.get("fishing")

    @JvmField
    val CAPTURE_MOB: HTUpgradeKey = HTUpgradeKey.get("capture_mob")
}
