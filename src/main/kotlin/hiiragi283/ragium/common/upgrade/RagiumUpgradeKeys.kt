package hiiragi283.ragium.common.upgrade

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.upgrade.HTUpgradeKey

object RagiumUpgradeKeys {
    //    Processor    //

    @JvmField
    val BLASTING: HTUpgradeKey = HTUpgradeKey.get(RagiumAPI.id("blasting"))

    @JvmField
    val SMOKING: HTUpgradeKey = HTUpgradeKey.get(RagiumAPI.id("smoking"))

    @JvmField
    val VOID_EXTRA: HTUpgradeKey = HTUpgradeKey.get(RagiumAPI.id("void_extra"))

    @JvmField
    val USE_LUBRICANT: HTUpgradeKey = HTUpgradeKey.get(RagiumAPI.id("use_lubricant"))

    //    Device    //
}
