package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.common.init.RagiumMaterialKeys
import net.minecraft.util.StringIdentifiable

enum class RagiumMaterialTranslations(val key: HTMaterialKey, override val enName: String, override val jaName: String) :
    HTTranslationProvider,
    StringIdentifiable {
    UNDEFINED(HTMaterialKey.of("undefined"), "UNDEFINED", "未定義"),

    // tier1
    CRUDE_RAGINITE(RagiumMaterialKeys.CRUDE_RAGINITE, "Crude Raginite", "粗製ラギナイト"),
    RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY, "Ragi-Alloy", "ラギ合金"),
    ASH(RagiumMaterialKeys.ASH, "Ash", "灰"),
    COPPER(RagiumMaterialKeys.COPPER, "Copper", "銅"),
    IRON(RagiumMaterialKeys.IRON, "Iron", "鉄"),
    NITER(RagiumMaterialKeys.NITER, "Niter", "硝石"),
    SULFUR(RagiumMaterialKeys.SULFUR, "Sulfur", "硫黄"),

    // tier2
    RAGINITE(RagiumMaterialKeys.RAGINITE, "Raginite", "ラギナイト"),
    RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL, "Ragi-Steel", "ラギスチール"),
    BAUXITE(RagiumMaterialKeys.BAUXITE, "Bauxite", "ボーキサイト"),
    FLUORITE(RagiumMaterialKeys.FLUORITE, "Fluorite", "蛍石"),
    GOLD(RagiumMaterialKeys.GOLD, "Gold", "金"),
    PLASTIC(RagiumMaterialKeys.PLASTIC, "Plastic", "プラスチック"), // PE
    SILICON(RagiumMaterialKeys.SILICON, "Silicon", "シリコン"),
    STEEL(RagiumMaterialKeys.STEEL, "Steel", "スチール"),

    // tier3
    RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL, "Ragi-Crystal", "ラギクリスタリル"),
    REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL, "Refined Ragi-Steel", "精製ラギスチール"),
    ALUMINUM(RagiumMaterialKeys.ALUMINUM, "Aluminum", "アルミニウム"),
    ENGINEERING_PLASTIC(RagiumMaterialKeys.ENGINEERING_PLASTIC, "Engineering Plastic", "エンジニアリングプラスチック"), // PC
    STELLA(RagiumMaterialKeys.STELLA, "S.T.E.L.L.A.", "S.T.E.L.L.A."),

    // tier4
    RAGIUM(RagiumMaterialKeys.RAGIUM, "Ragium", "ラギウム"),

    // integration
    EMERALD(RagiumMaterialKeys.RAGIUM, "Emerald", "エメラルド"),
    DIAMOND(RagiumMaterialKeys.RAGIUM, "Diamond", "ダイヤモンド"),
    LAPIS(RagiumMaterialKeys.RAGIUM, "Lapis", "ラピス"),
    PERIDOT(RagiumMaterialKeys.RAGIUM, "Peridot", "ペリドット"),
    QUARTZ(RagiumMaterialKeys.RAGIUM, "Quartz", "クォーツ"),
    RUBY(RagiumMaterialKeys.RAGIUM, "Ruby", "ルビー"),
    SAPPHIRE(RagiumMaterialKeys.RAGIUM, "Sapphire", "サファイア"),

    IRIDIUM(RagiumMaterialKeys.RAGIUM, "Iridium", "イリジウム"),
    LEAD(RagiumMaterialKeys.RAGIUM, "Lead", "鉛"),
    NICKEL(RagiumMaterialKeys.RAGIUM, "Nickel", "ニッケル"),
    PLATINUM(RagiumMaterialKeys.RAGIUM, "Platinum", "プラチナ"),
    SILVER(RagiumMaterialKeys.RAGIUM, "Silver", "シルバー"),
    TIN(RagiumMaterialKeys.RAGIUM, "Tin", "スズ"),
    TITANIUM(RagiumMaterialKeys.RAGIUM, "Titanium", "チタン"),
    TUNGSTEN(RagiumMaterialKeys.RAGIUM, "Tungsten", "タングステン"),
    ZINC(RagiumMaterialKeys.RAGIUM, "Zinc", "亜鉛"),

    BRASS(RagiumMaterialKeys.RAGIUM, "Brass", "真鍮"),
    BRONZE(RagiumMaterialKeys.RAGIUM, "Bronze", "青銅"),
    ELECTRUM(RagiumMaterialKeys.RAGIUM, "Electrum", "琥珀金"),
    INVAR(RagiumMaterialKeys.RAGIUM, "Invar", "インバー"),
    ;

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()
}
