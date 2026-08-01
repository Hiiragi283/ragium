package hiiragi283.ragium.data.advancement

import hiiragi283.core.api.data.advancement.AdvancementKey
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.material.RagiumMaterialKeys

object RagiumAdvancementKeys {
    @JvmStatic
    val ROOT: AdvancementKey = create("root")

    // Mechanical
    @JvmStatic
    val RAGI_ALLOY: AdvancementKey = create(RagiumMaterialKeys.RAGI_ALLOY)

    @JvmStatic
    val ALLOY_SMELTER: AdvancementKey = create(RagiumConst.ALLOY_SMELTER)

    // Heat
    @JvmStatic
    val ADVANCED_RAGI_ALLOY: AdvancementKey = create(RagiumMaterialKeys.ADVANCED_RAGI_ALLOY)

    @JvmStatic
    val HEATING_COIL: AdvancementKey = create("heating_coil")

    @JvmStatic
    val REFINERY: AdvancementKey = create(RagiumConst.REFINERY)

    @JvmStatic
    val PLASTIC: AdvancementKey = create(CommonMaterialKeys.PLASTIC)

    @JvmStatic
    val REFINED_SILICON: AdvancementKey = create("refined_silicon")

    @JvmStatic
    val PYROLYZER: AdvancementKey = create(RagiumConst.PYROLYZER)

    @JvmStatic
    val CRIMSON_CRYSTAL: AdvancementKey = create(HCMaterialKeys.CRIMSON_CRYSTAL)

    @JvmStatic
    val WARPED_CRYSTAL: AdvancementKey = create(HCMaterialKeys.WARPED_CRYSTAL)

    @JvmStatic
    val STAINLESS_STEEL: AdvancementKey = create(RagiumMaterialKeys.STAINLESS_STEEL)

    // Chemical
    @JvmStatic
    val THERMOMETER: AdvancementKey = create("thermometer")

    @JvmStatic
    val MIXER: AdvancementKey = create(RagiumConst.MIXER)

    // Bio
    @JvmStatic
    val BREWERY: AdvancementKey = create(RagiumConst.BREWERY)

    // Cold

    // Electronics
    @JvmStatic
    val RAGI_CRYSTAL: AdvancementKey = create(RagiumMaterialKeys.RAGI_CRYSTAL)

    @JvmStatic
    val ELECTRIC_CIRCUIT: AdvancementKey = create("electric_circuit")

    @JvmStatic
    val LASER_EMITTER: AdvancementKey = create("laser_emitter")

    @JvmStatic
    private fun create(key: HTMaterialKey): AdvancementKey = create(key.getId().path)

    @JvmStatic
    private fun create(path: String): AdvancementKey = AdvancementKey(RagiumAPI.id(path))
}
