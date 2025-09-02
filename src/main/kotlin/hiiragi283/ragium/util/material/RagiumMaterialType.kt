package hiiragi283.ragium.util.material

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.api.util.material.HTItemMaterialVariant
import hiiragi283.ragium.api.util.material.HTMaterialType

enum class RagiumMaterialType(private val enName: String, private val jpName: String) : HTMaterialType.Translatable {
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
    IRIDESCENTIUM("Iridescentium", "七色金"),

    // Food
    CHOCOLATE("Chocolate", "チョコレート"),
    MEAT("Meat", "生肉") {
        override fun translate(type: HTLanguageType, variant: HTVariantKey): String = if (variant == HTItemMaterialVariant.DUST) {
            when (type) {
                HTLanguageType.EN_US -> "Minced Meat"
                HTLanguageType.JA_JP -> "ひき肉"
            }
        } else {
            super.translate(type, variant)
        }
    },
    COOKED_MEAT("Cooked Meat", "焼肉"),

    // Other
    COAL_COKE("Coal Coke", "石炭コークス"),
    PLASTIC("Plastic", "プラスチック"),
    ;

    override val translationKey: String = "material.${RagiumAPI.MOD_ID}.$serializedName"

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jpName
    }

    override fun getSerializedName(): String = name.lowercase()
}
