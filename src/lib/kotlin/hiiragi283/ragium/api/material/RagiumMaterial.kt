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
            yieldAll(DustLike.entries)
            yieldAll(Gem.entries)
            yieldAll(MetalLike.entries)
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

    /**
     * @since 26.1.8
     */
    sealed interface DustLike : RagiumMaterial {
        companion object {
            @JvmField
            val entries: Sequence<DustLike> = sequence {
                yieldAll(Mineral.entries)
                yieldAll(Other.entries)
            }
        }
    }

    enum class Mineral(langName: HTLangName) :
        DustLike,
        HTLangName by langName {
        // Minecraft
        REDSTONE("Redstone", "レッドストーン"),
        GLOWSTONE("Glowstone", "グロウストーン"),

        // Common
        SALT("Salt", "食塩"),

        /**
         * @since 26.1.7
         */
        BAUXITE("Bauxite", "ボーキサイト"),

        SULFUR("Sulfur", "硫黄"),
        NITER("Niter", "硝石"),

        /**
         * @since 26.1.8
         */
        GYPSUM("Gypsum", "石膏"),

        // Ragium
        RAGINITE("Raginite", "ラギナイト"),
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        val isVanilla: Boolean get() = this == REDSTONE || this == GLOWSTONE

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
        PRISMARINE("Prismarine", "プリズマリン"),

        // Common

        /**
         * @since 26.1.8
         */
        CINNABAR("Cinnabar", "辰砂"),

        /**
         * @since 26.1.7
         */
        FLUORITE("Fluorite", "蛍石"),
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

    enum class Other(langName: HTLangName) :
        DustLike,
        HTLangName by langName {
        // Minecraft
        WOOD("Wood", "木") {
            override val isPulp: Boolean = true
        },
        OBSIDIAN("Obsidian", "黒曜石"),
        PAPER("Paper", "紙") {
            override val isPulp: Boolean = true
        },

        // Common
        CARBON("Carbon", "炭素"),

        /**
         * @since 26.1.7
         */
        ALUMINA("Alumina", "アルミナ"),

        /**
         * @since 26.1.7
         */
        SILICON("Silicon", "シリコン"),

        /**
         * @since 26.1.11
         */
        SILICA("Silica", "シリカ"),

        /**
         * @since 26.1.9
         */
        ASH("Ash", "灰"),

        /**
         * @since 26.1.8
         */
        LIME("Lime", "石灰"),

        /**
         * @since 26.1.8
         */
        QUICK_LIME("Quick Lime", "生石灰"),
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        open val isPulp: Boolean = false

        override val materialName: String = name.lowercase()
    }
}
