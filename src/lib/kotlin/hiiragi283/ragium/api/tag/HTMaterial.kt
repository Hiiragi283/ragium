package hiiragi283.ragium.api.tag

import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.tag.HTMaterialLike

sealed interface HTMaterial :
    HTMaterialLike,
    HTLangName {

    enum class Fuels(langName: HTLangName) :
        HTMaterial,
        HTLangName by langName {
        COAL("Coal", "石炭"),
        CHARCOAL("Charcoal", "木炭"),
        COAL_COKE("Coal Coke", "石炭コークス"),
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        override val materialName: String = name.lowercase()
    }

    enum class Minerals(langName: HTLangName) :
        HTMaterial,
        HTLangName by langName {
        REDSTONE("Redstone", "レッドストーン"),
        GLOWSTONE("Glowstone", "グロウストーン"),
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        override val materialName: String = name.lowercase()
    }

    enum class Gems(langName: HTLangName) :
        HTMaterial,
        HTLangName by langName {
        LAPIS("Lapis", "ラピス"),
        QUARTZ("Quartz", "水晶"),
        AMETHYST("Amethyst", "アメジスト"),
        DIAMOND("Diamond", "ダイヤモンド"),
        EMERALD("Emerald", "エメラルド"),
        ECHO("Echo", "残響"),
        PRISMARINE("Prismarine", "プリズマリン"),
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        override val materialName: String = name.lowercase()
    }

    enum class Metal(val isElement: Boolean, langName: HTLangName) :
        HTMaterial,
        HTLangName by langName {
        COPPER(true, "Copper", "銅"),
        IRON(true, "Iron", "鉄"),
        GOLD(true, "Gold", "金"),
        NETHERITE(false, "Netherite", "ネザライト"),
        ;

        constructor(isElement: Boolean, enName: String, jaName: String) : this(isElement, HTLangName(enName, jaName))

        override val materialName: String = name.lowercase()
    }

    enum class Other(langName: HTLangName) :
        HTMaterial,
        HTLangName by langName {
        WOOD("Wood", "木"),
        GLASS("Glass", "ガラス"),
        OBSIDIAN("Obsidian", "黒曜石"),
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        override val materialName: String = name.lowercase()
    }
}
