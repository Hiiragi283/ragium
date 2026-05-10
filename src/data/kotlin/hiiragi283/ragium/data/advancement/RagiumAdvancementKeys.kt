package hiiragi283.ragium.data.advancement

import hiiragi283.core.api.data.advancement.HTAdvancementKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.material.RagiumMaterialKeys

object RagiumAdvancementKeys {
    @JvmStatic
    val ROOT: HTAdvancementKey = create("root")

    // Basic
    @JvmStatic
    val RAGI_ALLOY: HTAdvancementKey = create(RagiumMaterialKeys.RAGI_ALLOY)

    @JvmStatic
    val ALLOY_SMELTER: HTAdvancementKey = create(RagiumConst.ALLOY_SMELTER)

    // Advanced
    @JvmStatic
    val THERMOMETER: HTAdvancementKey = create("thermometer")

    @JvmStatic
    val ADVANCED_RAGI_ALLOY: HTAdvancementKey = create(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)

    @JvmStatic
    val REFINERY: HTAdvancementKey = create(RagiumConst.REFINERY)

    @JvmStatic
    val PLASTIC: HTAdvancementKey = create(CommonMaterialKeys.PLASTIC)

    @JvmStatic
    val REFINED_SILICON: HTAdvancementKey = create("refined_silicon")

    @JvmStatic
    val PYROLYZER: HTAdvancementKey = create(RagiumConst.PYROLYZER)

    @JvmStatic
    val CRIMSON_CRYSTAL: HTAdvancementKey = create(HCMaterialKeys.CRIMSON_CRYSTAL)

    @JvmStatic
    val WARPED_CRYSTAL: HTAdvancementKey = create(HCMaterialKeys.WARPED_CRYSTAL)

    // Elite
    @JvmStatic
    val RAGI_CRYSTAL: HTAdvancementKey = create(RagiumMaterialKeys.RAGI_CRYSTAL)

    @JvmStatic
    val STAINLESS_STEEL: HTAdvancementKey = create(RagiumMaterialKeys.STAINLESS_STEEL)

    @JvmStatic
    val ELECTRIC_CIRCUIT: HTAdvancementKey = create("electric_circuit")

    @JvmStatic
    val BREWERY: HTAdvancementKey = create(RagiumConst.BREWERY)

    @JvmStatic
    val MIXER: HTAdvancementKey = create(RagiumConst.MIXER)

    // Ultimate

    @JvmStatic
    private fun create(material: HTMaterialLike): HTAdvancementKey = create(material.asMaterialId().path)

    @JvmStatic
    private fun create(path: String): HTAdvancementKey = HTAdvancementKey.of(RagiumAPI.id(path))
}
