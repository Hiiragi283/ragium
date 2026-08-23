package hiiragi283.ragium.api.tag

import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.ragium.api.RagiumAPI

sealed interface HTMaterial :
    HTMaterialLike,
    HTLangName,
    Comparable<HTMaterial> {

    override fun compareTo(other: HTMaterial): Int = this.materialName.compareTo(other.materialName)

    enum class Fuel(langName: HTLangName) :
        HTMaterial,
        HTLangName by langName {
        // Minecraft
        COAL("Coal", "石炭"),
        CHARCOAL("Charcoal", "木炭"),

        // Common
        COAL_COKE("Coal Coke", "石炭コークス"),
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        val baseItem: HTSimpleDeferredItem get() = when (this) {
            COAL -> vanillaId("coal")
            CHARCOAL -> vanillaId("charcoal")
            COAL_COKE -> RagiumAPI.id("coal_coke")
        }.let(::HTSimpleDeferredItem)

        override val materialName: String = name.lowercase()
    }

    enum class Mineral(langName: HTLangName) :
        HTMaterial,
        HTLangName by langName {
        // Minecraft
        REDSTONE("Redstone", "レッドストーン"),
        GLOWSTONE("Glowstone", "グロウストーン"),

        // Common
        SALT("Salt", "食塩"),
        NITER("Niter", "硝石"),
        BORAX("Borax", "ホウ砂"),

        // Ragium
        RAGINITE("Raginite", "ラギナイト"),
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        override val materialName: String = name.lowercase()
    }

    enum class Gem(langName: HTLangName) :
        HTMaterial,
        HTLangName by langName {
        // Minecraft
        LAPIS("Lapis", "ラピス"),
        QUARTZ("Quartz", "水晶"),
        AMETHYST("Amethyst", "アメジスト"),
        DIAMOND("Diamond", "ダイヤモンド"),
        EMERALD("Emerald", "エメラルド"),
        ECHO("Echo", "残響"),
        PRISMARINE("Prismarine", "プリズマリン"),

        // Ragium
        RAGI_CRYSTAL("Ragi-Crystal", "ラギクリスタリル"),
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        override val materialName: String = name.lowercase()
    }

    enum class Metal(val isElement: Boolean, langName: HTLangName) :
        HTMaterial,
        HTLangName by langName {
        // Minecraft
        COPPER(true, "Copper", "銅"),
        IRON(true, "Iron", "鉄"),
        GOLD(true, "Gold", "金"),
        NETHERITE(false, "Netherite", "ネザライト"),

        // Common
        STEEL(false, "Steel", "鋼鉄"),

        // Ragium
        RAGI_ALLOY(false, "Ragi-Alloy", "ラギ合金"),
        ADVANCED_RAGI_ALLOY(false, "Advanced Ragi-Alloy", "発展ラギ合金"),
        ;

        constructor(isElement: Boolean, enName: String, jaName: String) : this(isElement, HTLangName(enName, jaName))

        override val materialName: String = name.lowercase()
    }

    enum class Other(val isPulp: Boolean, langName: HTLangName) :
        HTMaterial,
        HTLangName by langName {
        // Minecraft
        WOOD(true, "Wood", "木"),
        GLASS(false, "Glass", "ガラス"),
        OBSIDIAN(false, "Obsidian", "黒曜石"),
        PAPER(true, "Paper", "紙"),

        // Common
        SILICON(false, "Silicon", "シリコン"),
        ;

        constructor(isPulp: Boolean, enName: String, jaName: String) : this(isPulp, HTLangName(enName, jaName))

        override val materialName: String = name.lowercase()
    }
}
