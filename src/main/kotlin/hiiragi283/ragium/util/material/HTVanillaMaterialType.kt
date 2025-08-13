package hiiragi283.ragium.util.material

import hiiragi283.ragium.api.data.HTLanguageType
import hiiragi283.ragium.api.util.material.HTMaterialType

enum class HTVanillaMaterialType(private val enName: String, private val jpName: String) : HTMaterialType {
    // Metal
    COPPER("Copper", "銅"),
    IRON("Iron", "鉄"),
    GOLD("Gold", "金"),
    NETHERITE("Netherite", "ネザライト"),

    // Gem
    DIAMOND("Diamond", "ダイアモンド"),
    EMERALD("Emerald", "エメラルド"),
    QUARTZ("Quartz", "水晶"),

    // Other
    SOUL("Soul", "ソウル"),
    OBSIDIAN("Obsidian", "黒曜石"),
    ;

    override fun getTranslatedName(type: HTLanguageType): String = when (type) {
        HTLanguageType.EN_US -> enName
        HTLanguageType.JA_JP -> jpName
    }

    override fun getSerializedName(): String = name.lowercase()
}
