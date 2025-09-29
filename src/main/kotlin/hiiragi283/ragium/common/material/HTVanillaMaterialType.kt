package hiiragi283.ragium.common.material

import hiiragi283.ragium.api.data.lang.HTLanguageType
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.variant.HTVariantKey

enum class HTVanillaMaterialType(private val enName: String, private val jpName: String) : HTMaterialType.Translatable {
    // Metal
    COPPER("Copper", "銅"),
    IRON("Iron", "鉄"),
    GOLD("Gold", "金"),
    NETHERITE("Netherite", "ネザライト"),

    // Gem
    LAPIS("Lapis", "ラピス"),
    QUARTZ("Quartz", "水晶"),
    AMETHYST("Amethyst", "アメシスト"),
    DIAMOND("Diamond", "ダイアモンド"),
    EMERALD("Emerald", "エメラルド"),
    ECHO("Echo", "残響"),

    // Other
    COAL("Coal", "石炭"),
    CHARCOAL("Charcoal", "木炭"),
    REDSTONE("Redstone", "レッドストーン"),
    GLOWSTONE("Glowstone", "グロウストーン"),
    SOUL("Soul", "ソウル"),
    OBSIDIAN("Obsidian", "黒曜石"),
    WOOD("Wood", "木") {
        override fun translate(type: HTLanguageType, variant: HTVariantKey): String = if (variant == HTItemMaterialVariant.DUST) {
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
