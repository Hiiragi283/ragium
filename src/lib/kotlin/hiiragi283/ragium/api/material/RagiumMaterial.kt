package hiiragi283.ragium.api.material

import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.tag.HTMaterialLike

sealed interface RagiumMaterial :
    HTMaterialLike,
    HTLangName {
    companion object {
        @JvmField
        val entries: Sequence<RagiumMaterial> = sequence {
            yieldAll(Fuel.entries)
            yieldAll(Mineral.entries)
            yieldAll(Gem.entries)
            yieldAll(Metal.entries)
            yieldAll(Other.entries)
        }

        @JvmField
        val COMPARATOR: Comparator<RagiumMaterial> = compareBy(RagiumMaterial::materialName)
    }

    enum class Fuel(langName: HTLangName) :
        RagiumMaterial,
        HTLangName by langName {
        // Minecraft
        COAL("Coal", "石炭"),
        CHARCOAL("Charcoal", "木炭"),

        // Common
        COAL_COKE("Coal Coke", "石炭コークス")
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        override val materialName: String = name.lowercase()
    }

    enum class Mineral(langName: HTLangName) :
        RagiumMaterial,
        HTLangName by langName {
        // Minecraft
        REDSTONE("Redstone", "レッドストーン") {
            override val isVanilla: Boolean = true
        },
        GLOWSTONE("Glowstone", "グロウストーン") {
            override val isVanilla: Boolean = true
        },

        // Common
        SALT("Salt", "食塩"),
        SULFUR("Sulfur", "硫黄"),
        NITER("Niter", "硝石"),
        BORAX("Borax", "ホウ砂"),

        // Ragium
        RAGINITE("Raginite", "ラギナイト")
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        open val isVanilla: Boolean = false

        override val materialName: String = name.lowercase()
    }

    enum class Gem(langName: HTLangName) :
        RagiumMaterial,
        HTLangName by langName {
        // Minecraft
        LAPIS("Lapis", "ラピス"),
        QUARTZ("Quartz", "水晶"),
        AMETHYST("Amethyst", "アメジスト"),
        DIAMOND("Diamond", "ダイヤモンド"),
        EMERALD("Emerald", "エメラルド"),
        ECHO("Echo", "残響"),
        PRISMARINE("Prismarine", "プリズマリン")
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        override val materialName: String = name.lowercase()
    }

    enum class Metal(langName: HTLangName) :
        RagiumMaterial,
        HTLangName by langName {
        // Minecraft
        COPPER("Copper", "銅"),
        IRON("Iron", "鉄"),
        GOLD("Gold", "金"),
        NETHERITE("Netherite", "ネザライト") {
            override val isElement: Boolean = false
        },

        // Ragium

        /**
         * @since 26.1.3
         */
        SOOTY_IRON("Sooty Iron", "煤鉄") {
            override val isElement: Boolean = false
        },

        /**
         * @since 26.1.3
         */
        BLACK_STEEL("Black Steel", "黒鋼") {
            override val isElement: Boolean = false
        }
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        open val isElement: Boolean = true

        override val materialName: String = name.lowercase()
    }

    enum class Other(val isPulp: Boolean, langName: HTLangName) :
        RagiumMaterial,
        HTLangName by langName {
        // Minecraft
        WOOD(true, "Wood", "木"),
        GLASS(false, "Glass", "ガラス"),
        OBSIDIAN(false, "Obsidian", "黒曜石"),
        PAPER(true, "Paper", "紙"),

        // Common
        SILICON(false, "Silicon", "シリコン")
        ;

        constructor(isPulp: Boolean, enName: String, jaName: String) : this(isPulp, HTLangName(enName, jaName))

        override val materialName: String = name.lowercase()
    }
}
