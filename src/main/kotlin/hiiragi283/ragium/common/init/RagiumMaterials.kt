package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.util.HTTranslationProvider

enum class RagiumMaterials(val tier: HTMachineTier, override val enName: String, override val jaName: String) : HTTranslationProvider {
    // tier1
    RAW_RAGINITE(HTMachineTier.PRIMITIVE, "Raw Raginite", "未加工のラギナイト"),
    RAGI_ALLOY(HTMachineTier.PRIMITIVE, "Ragi-Alloy", "ラギ合金"),
    COPPER(HTMachineTier.PRIMITIVE, "Copper", "銅"),
    IRON(HTMachineTier.PRIMITIVE, "Iron", "鉄"),
    WOOD(HTMachineTier.PRIMITIVE, "Wooden", "木"),

    // tier2
    RAGINITE(HTMachineTier.BASIC, "Raginite", "ラギナイト"),
    RAGI_STEEL(HTMachineTier.BASIC, "Ragi-Steel", "ラギスチール"),
    ASH(HTMachineTier.BASIC, "Ash", "灰"),
    GOLD(HTMachineTier.BASIC, "Gold", "金"),
    RUBBER(HTMachineTier.BASIC, "Rubber", "ゴム"),
    STEEL(HTMachineTier.BASIC, "Steel", "スチール"),
    SULFUR(HTMachineTier.BASIC, "Sulfur", "硫黄"),

    // tier3
    RAGI_CRYSTAL(HTMachineTier.ADVANCED, "Ragi-Crystal", "ラギクリスタリル"),
    REFINED_RAGI_STEEL(HTMachineTier.ADVANCED, "Refined Ragi-Steel", "精製ラギスチール"),
    NETHERITE(HTMachineTier.ADVANCED, "Netherite", "ネザライト"),
    PE(HTMachineTier.ADVANCED, "PE", "ポリエチレン"),
    PVC(HTMachineTier.ADVANCED, "PVC", "塩化ビニル"),
    PTFE(HTMachineTier.ADVANCED, "PTFE", "テフロン"),
    ;

    fun getBlock(): RagiumContents.StorageBlocks? = RagiumContents.StorageBlocks.entries.firstOrNull { it.material == this }

    fun getHull(): RagiumContents.Hulls? = RagiumContents.Hulls.entries.firstOrNull { it.material == this }

    fun getIngot(): RagiumContents.Ingots? = RagiumContents.Ingots.entries.firstOrNull { it.material == this }

    fun getPlate(): RagiumContents.Plates? = RagiumContents.Plates.entries.firstOrNull { it.material == this }
}
