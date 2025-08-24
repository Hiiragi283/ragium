package hiiragi283.ragium.util.material

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.util.material.HTItemMaterialVariant
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant

enum class RagiumMaterialType(private val enName: String, private val jpName: String, val baseVariant: HTMaterialVariant.ItemTag? = null) :
    HTMaterialType {
    // Mineral
    RAGINITE("Raginite", "ラギナイト"),
    CINNABAR("Cinnabar", "辰砂"),
    SALTPETER("Saltpeter", "硝石"),
    SULFUR("Sulfur", "硫黄"),

    // Gem
    RAGI_CRYSTAL("Ragi-Crystal", "ラギクリスタリル", HTItemMaterialVariant.GEM),
    AZURE("Azure Shard", "紺碧の欠片", HTItemMaterialVariant.GEM),
    CRIMSON_CRYSTAL("Crimson Crystal", "深紅の結晶", HTItemMaterialVariant.GEM),
    WARPED_CRYSTAL("Warped Crystal", "歪んだ結晶", HTItemMaterialVariant.GEM),
    ELDRITCH_PEARL("Eldritch Pearl", "異質な真珠", HTItemMaterialVariant.GEM),

    // Metal
    RAGI_ALLOY("Ragi-Alloy", "ラギ合金", HTItemMaterialVariant.INGOT),
    ADVANCED_RAGI_ALLOY("Advanced Ragi-Alloy", "発展ラギ合金", HTItemMaterialVariant.INGOT),
    AZURE_STEEL("Azure Steel", "紺鉄", HTItemMaterialVariant.INGOT),
    DEEP_STEEL("Deep Steel", "深層鋼", HTItemMaterialVariant.INGOT),
    IRIDESCENTIUM("Iridescentium", "七色金", HTItemMaterialVariant.INGOT),

    // Food
    CHOCOLATE("Chocolate", "チョコレート", HTItemMaterialVariant.INGOT),
    MEAT("Meat", "生肉", HTItemMaterialVariant.INGOT),
    COOKED_MEAT("Cooked Meat", "焼肉", HTItemMaterialVariant.INGOT),

    // Other
    ASH("Ash", "灰"),
    COAL_COKE("Coal Coke", "石炭コークス", HTItemMaterialVariant.FUEL),
    PLASTIC("Plastic", "プラスチック", HTItemMaterialVariant.PLATE),
    WOOD("Wood", "木") {
        override fun translate(type: HTLanguageType, variant: HTMaterialVariant): String = if (variant == HTItemMaterialVariant.DUST) {
            when (type) {
                HTLanguageType.EN_US -> "Sawdust"
                HTLanguageType.JA_JP -> "おがくず"
            }
        } else {
            super.translate(type, variant)
        }
    },
    ;

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jpName
    }

    override fun getSerializedName(): String = name.lowercase()
}
