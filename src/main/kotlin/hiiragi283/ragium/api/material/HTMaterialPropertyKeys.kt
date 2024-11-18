package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyKey

object HTMaterialPropertyKeys {
    @JvmField
    val DISABLE_BLOCK_CRAFTING: HTPropertyKey.Defaulted<Unit> =
        HTPropertyKey.ofFlag(RagiumAPI.id("disable_block_crafting"))

    @JvmField
    val DISABLE_DUST_SMELTING: HTPropertyKey.Defaulted<Unit> =
        HTPropertyKey.ofFlag(RagiumAPI.id("disable_dust_smelting"))

    @JvmField
    val DISABLE_RAW_SMELTING: HTPropertyKey.Defaulted<Unit> =
        HTPropertyKey.ofFlag(RagiumAPI.id("disable_raw_smelting"))

    @JvmField
    val GRINDING_BASE_COUNT: HTPropertyKey.Defaulted<Int> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("grinding_count"), 1)
    
    @JvmField
    val SMELTING_EXP: HTPropertyKey.Defaulted<Float> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("smelting_exp"), 0f)
}
