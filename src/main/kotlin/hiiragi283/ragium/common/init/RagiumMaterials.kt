package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.util.HTTranslationProvider

enum class RagiumMaterials(val tier: HTMachineTier, override val enName: String, override val jaName: String) : HTTranslationProvider {
    // tier1
    RAW_RAGINITE(HTMachineTier.PRIMITIVE, "Raw Raginite", "未加工のラギナイト"),
    RAGI_ALLOY(HTMachineTier.PRIMITIVE, "Ragi-Alloy", "ラギ合金"),
    COPPER(HTMachineTier.PRIMITIVE, "Copper", "銅"),
    IRON(HTMachineTier.PRIMITIVE, "Iron", "鉄"),
    ASH(HTMachineTier.BASIC, "Ash", "灰"),
    NITER(HTMachineTier.PRIMITIVE, "Niter", "硝石"),
    SULFUR(HTMachineTier.BASIC, "Sulfur", "硫黄"),

    // tier2
    RAGINITE(HTMachineTier.BASIC, "Raginite", "ラギナイト"),
    RAGI_STEEL(HTMachineTier.BASIC, "Ragi-Steel", "ラギスチール"),
    BASALT_FIBER(HTMachineTier.BASIC, "Basalt Fiber", "玄武岩繊維"),
    GOLD(HTMachineTier.BASIC, "Gold", "金"),
    INVAR(HTMachineTier.BASIC, "Invar", "インバー"),
    NICKEL(HTMachineTier.BASIC, "Nickel", "ニッケル"),
    SILICON(HTMachineTier.BASIC, "Silicon", "シリコン"),
    SILVER(HTMachineTier.BASIC, "Silver", "銀"),
    STEEL(HTMachineTier.BASIC, "Steel", "スチール"),

    // tier3
    RAGI_CRYSTAL(HTMachineTier.ADVANCED, "Ragi-Crystal", "ラギクリスタリル"),
    REFINED_RAGI_STEEL(HTMachineTier.ADVANCED, "Refined Ragi-Steel", "精製ラギスチール"),
    PE(HTMachineTier.ADVANCED, "PE", "ポリエチレン"),
    PVC(HTMachineTier.ADVANCED, "PVC", "塩化ビニル"),
    PTFE(HTMachineTier.ADVANCED, "PTFE", "テフロン"),

    ;

    fun getOre(): RagiumContents.Ores? = RagiumContents.Ores.entries.firstOrNull { it.material == this }

    fun getDeepOre(): RagiumContents.DeepOres? = RagiumContents.DeepOres.entries.firstOrNull { it.material == this }

    fun getBlock(): RagiumContents.StorageBlocks? = RagiumContents.StorageBlocks.entries.firstOrNull { it.material == this }

    fun getHull(): RagiumContents.Hulls? = RagiumContents.Hulls.entries.firstOrNull { it.material == this }

    fun getIngot(): RagiumContents.Ingots? = RagiumContents.Ingots.entries.firstOrNull { it.material == this }

    fun getPlate(): RagiumContents.Plates? = RagiumContents.Plates.entries.firstOrNull { it.material == this }

    fun getRawMaterial(): RagiumContents.RawMaterials? = RagiumContents.RawMaterials.entries.firstOrNull { it.material == this }
}
