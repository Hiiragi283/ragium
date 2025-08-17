package hiiragi283.ragium.util.material

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant

enum class RagiumMaterialType(private val enName: String, private val jpName: String) : HTMaterialType {
    // Mineral
    RAGINITE("Raginite", "ラギナイト"),
    CINNABAR("Cinnabar", "辰砂"),
    SALTPETER("Saltpeter", "硝石"),
    SULFUR("Sulfur", "硫黄"),

    // Gem
    RAGI_CRYSTAL("Ragi-Crystal", "ラギクリスタリル"),
    AZURE("Azure Shard", "紺碧の欠片"),
    CRIMSON_CRYSTAL("Crimson Crystal", "深紅の結晶"),
    WARPED_CRYSTAL("Warped Crystal", "歪んだ結晶"),
    ELDRITCH_PEARL("Eldritch Pearl", "異質な真珠"),

    // Metal
    RAGI_ALLOY("Ragi-Alloy", "ラギ合金"),
    ADVANCED_RAGI_ALLOY("Advanced Ragi-Alloy", "発展ラギ合金"),
    AZURE_STEEL("Azure Steel", "紺鉄"),
    DEEP_STEEL("Deep Steel", "深層鋼"),

    // Food
    CHOCOLATE("Chocolate", "チョコレート"),
    MEAT("Meat", "生肉"),
    COOKED_MEAT("Cooked Meat", "焼肉"),

    // Other
    ASH("Ash", "灰"),
    COAL_COKE("Coal Coke", "石炭コークス"),
    PLASTIC("Plastic", "プラスチック"),
    WOOD("Wood", "木") {
        override fun translate(type: HTLanguageType, variant: HTMaterialVariant): String = if (variant == HTMaterialVariant.DUST) {
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
