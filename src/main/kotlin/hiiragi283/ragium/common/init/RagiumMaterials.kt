package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.util.HTTranslationProvider

enum class RagiumMaterials(val tier: HTMachineTier, override val enName: String, override val jaName: String) : HTTranslationProvider {
    RAW_RAGINITE(HTMachineTier.HEAT, "Raw Raginite", "未加工のラギナイト"),
    RAGI_ALLOY(HTMachineTier.HEAT, "Ragi-Alloy", "ラギ合金"),
    RAGI_STEEL(HTMachineTier.ELECTRIC, "Ragi-Steel", "ラギスチール"),
    STEEL(HTMachineTier.ELECTRIC, "Steel", "スチール"),
    TWILIGHT_METAL(HTMachineTier.ELECTRIC, "Twilight Metal", "黄昏合金"),
    REFINED_RAGI_STEEL(HTMachineTier.CHEMICAL, "Refined Ragi-Steel", "精製ラギスチール"),
    PE(HTMachineTier.CHEMICAL, "PE", "ポリエチレン"),
    PVC(HTMachineTier.CHEMICAL, "PVC", "塩化ビニル"),
    PTFE(HTMachineTier.CHEMICAL, "PTFE", "テフロン"),
    ;

    fun getBlock(): RagiumContents.StorageBlocks? = RagiumContents.StorageBlocks.entries.firstOrNull { it.material == this }

    fun getHull(): RagiumContents.Hulls? = RagiumContents.Hulls.entries.firstOrNull { it.material == this }

    fun getIngot(): RagiumContents.Ingots? = RagiumContents.Ingots.entries.firstOrNull { it.material == this }

    fun getPlate(): RagiumContents.Plates? = RagiumContents.Plates.entries.firstOrNull { it.material == this }
}
