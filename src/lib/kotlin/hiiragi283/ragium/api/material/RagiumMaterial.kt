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
            yieldAll(Gem.entries)
            yieldAll(MetalLike.entries)
            yieldAll(DustLike.entries)
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
        COAL_COKE("Coal Coke", "石炭コークス"),

        /**
         * @since 26.1.7
         */
        PITCH_COKE("Pitch Coke", "ピッチコークス"),
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

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
        // PRISMARINE("Prismarine", "プリズマリン"),

        // Common

        /**
         * @since 26.1.8
         */
        CINNABAR("Cinnabar", "辰砂"), // HgS

        /**
         * @since 26.1.7
         */
        FLUORITE("Fluorite", "蛍石"), // CaF2

        /**
         * @since 26.1.12
         */
        APATITE("Apatite", "燐灰石"), // Ca3(PO4)2
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        override val materialName: String = name.lowercase()
    }

    /**
     * @since 26.1.8
     */
    sealed interface MetalLike : RagiumMaterial {
        companion object {
            @JvmField
            val entries: Sequence<MetalLike> = sequence {
                yieldAll(Metal.entries)
                yieldAll(Alloy.entries)
            }
        }
    }

    enum class Metal(langName: HTLangName) :
        MetalLike,
        HTLangName by langName {
        // Minecraft
        COPPER("Copper", "銅"),
        IRON("Iron", "鉄"),
        GOLD("Gold", "金"),

        // Common

        /**
         * @since 26.1.7
         */
        ALUMINUM("Aluminum", "アルミニウム"),

        /**
         * @since 26.1.11
         */
        NICKEL("Nickel", "ニッケル"),

        /**
         * @since 26.1.11
         */
        SILVER("Silver", "銀"),
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        override val materialName: String = name.lowercase()
    }

    enum class Alloy(langName: HTLangName) :
        MetalLike,
        HTLangName by langName {
        // Minecraft
        NETHERITE("Netherite", "ネザライト"),

        // Common

        /**
         * @since 26.1.11
         */
        CONSTANTAN("Constantan", "コンスタンタン"),

        // Ragium

        /**
         * @since 26.1.3
         */
        SOOTY_IRON("Sooty Iron", "煤鉄"),

        /**
         * @since 26.1.3
         */
        BLACK_STEEL("Black Steel", "黒鋼"),

        /**
         * @since 26.1.11
         */
        SHADOW_STEEL("Shadow Steel", "影鋼"),
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        override val materialName: String = name.lowercase()
    }

    /**
     * @since 26.1.8
     */
    sealed interface DustLike : RagiumMaterial {
        companion object {
            @JvmField
            val entries: Sequence<DustLike> = sequence {
                yieldAll(Chemical.entries)
                yieldAll(Other.entries)
            }
        }
    }

    enum class Chemical(langName: HTLangName) :
        DustLike,
        HTLangName by langName {
        // Common
        CARBON("Carbon", "炭素"), // C

        SALT("Salt", "食塩"), // NaCl

        /**
         * @since 26.1.7
         */
        BAUXITE("Bauxite", "ボーキサイト"), // Al2O3

        /**
         * @since 26.1.7
         */
        ALUMINA("Alumina", "アルミナ"), // Al2O3

        /**
         * @since 26.1.7
         */
        SILICON("Silicon", "シリコン"), // Si

        /**
         * @since 26.1.11
         */
        SILICA("Silica", "シリカ"), // SiO2

        SULFUR("Sulfur", "硫黄"), // S

        NITER("Niter", "硝石"), // KNO3

        /**
         * @since 26.1.9
         */
        ASH("Ash", "灰"), // K2CO3

        /**
         * @since 26.1.8
         */
        LIME("Lime", "石灰"), // CaCO3

        /**
         * @since 26.1.8
         */
        QUICK_LIME("Quick Lime", "生石灰"), // CaO

        /**
         * @since 26.1.8
         */
        GYPSUM("Gypsum", "石膏"), // CaSO4
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        override val materialName: String = name.lowercase()
    }

    enum class Other(langName: HTLangName) :
        DustLike,
        HTLangName by langName {
        // Minecraft
        WOOD("Wood", "木"),
        PAPER("Paper", "紙"),

        OBSIDIAN("Obsidian", "黒曜石"),
        REDSTONE("Redstone", "レッドストーン"),
        GLOWSTONE("Glowstone", "グロウストーン"),

        // Ragium
        RAGINITE("Raginite", "ラギナイト"),
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        val isPulp: Boolean get() = this == WOOD || this == PAPER

        val hasExistingDust: Boolean get() = this == REDSTONE || this == GLOWSTONE

        override val materialName: String = name.lowercase()
    }
}
