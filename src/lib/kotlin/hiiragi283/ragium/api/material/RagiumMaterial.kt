package hiiragi283.ragium.api.material

import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.ragium.api.data.chemical.HTChemical
import hiiragi283.ragium.api.data.chemical.RagiumChemicals
import net.minecraft.resources.ResourceKey

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

    val chemicalKey: ResourceKey<HTChemical>?

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
        PITCH_COKE("Pitch Coke", "ピッチコークス")
        ;

        constructor(enName: String, jaName: String) : this(HTLangName(enName, jaName))

        override val chemicalKey: ResourceKey<HTChemical> = RagiumChemicals.CARBON

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
                yieldAll(Chemicals.entries)
                yieldAll(Other.entries)
            }
        }
    }

    enum class Mineral(langName: HTLangName, override val chemicalKey: ResourceKey<HTChemical>?) :
        DustLike,
        HTLangName by langName {
        // Minecraft
        REDSTONE("Redstone", "レッドストーン"),
        GLOWSTONE("Glowstone", "グロウストーン"),

        // Common
        BORAX("Borax", "ホウ砂"),
        SALT("Salt", "食塩", RagiumChemicals.SODIUM_CHLORIDE),

        /**
         * @since 26.1.7
         */
        BAUXITE("Bauxite", "ボーキサイト", RagiumChemicals.ALUMINUM_OXIDE),

        SULFUR("Sulfur", "硫黄", RagiumChemicals.SULFUR),
        NITER("Niter", "硝石", RagiumChemicals.POTASSIUM_NITRATE),

        /**
         * @since 26.1.8
         */
        GYPSUM("Gypsum", "石膏", RagiumChemicals.CALCIUM_SULFATE),

        // Ragium
        RAGINITE("Raginite", "ラギナイト")
        ;

        constructor(enName: String, jaName: String, chemicalKey: ResourceKey<HTChemical>? = null) : this(
            HTLangName(enName, jaName),
            chemicalKey
        )

        val isVanilla: Boolean get() = this == REDSTONE || this == GLOWSTONE

        override val materialName: String = name.lowercase()
    }

    /**
     * @since 26.1.8
     */
    enum class Chemicals(langName: HTLangName, override val chemicalKey: ResourceKey<HTChemical>?) :
        DustLike,
        HTLangName by langName {
        // Common
        CARBON("Carbon", "炭素", RagiumChemicals.CARBON),

        /**
         * @since 26.1.8
         */
        SODA_ASH("Soda Ash", "ソーダ灰", RagiumChemicals.SODIUM_CARBONATE),

        /**
         * @since 26.1.7
         */
        ALUMINA("Alumina", "アルミナ", RagiumChemicals.ALUMINUM_OXIDE),

        /**
         * @since 26.1.7
         */
        SILICON("Silicon", "シリコン", RagiumChemicals.SILICON),

        /**
         * @since 26.1.9
         */
        ASH("Ash", "灰", RagiumChemicals.POTASSIUM_CARBONATE),

        /**
         * @since 26.1.8
         */
        LIME("Lime", "石灰", RagiumChemicals.CALCIUM_CARBONATE),

        /**
         * @since 26.1.8
         */
        QUICK_LIME("Quick Lime", "生石灰", RagiumChemicals.CALCIUM_OXIDE)
        ;

        constructor(enName: String, jaName: String, chemicalKey: ResourceKey<HTChemical>? = null) : this(
            HTLangName(enName, jaName),
            chemicalKey
        )

        override val materialName: String = name.lowercase()
    }

    enum class Gem(langName: HTLangName, override val chemicalKey: ResourceKey<HTChemical>?) :
        RagiumMaterial,
        HTLangName by langName {
        // Minecraft
        LAPIS("Lapis", "ラピス"),
        QUARTZ("Quartz", "水晶", RagiumChemicals.SILICON_DIOXIDE),
        AMETHYST("Amethyst", "アメジスト", RagiumChemicals.SILICON_DIOXIDE),
        DIAMOND("Diamond", "ダイヤモンド", RagiumChemicals.DIAMOND),
        EMERALD("Emerald", "エメラルド"),
        ECHO("Echo", "残響"),
        PRISMARINE("Prismarine", "プリズマリン"),

        // Common

        /**
         * @since 26.1.8
         */
        CINNABAR("Cinnabar", "辰砂", RagiumChemicals.MERCURY_SULFIDE),

        /**
         * @since 26.1.7
         */
        FLUORITE("Fluorite", "蛍石", RagiumChemicals.FLUORITE)
        ;

        constructor(enName: String, jaName: String, chemicalKey: ResourceKey<HTChemical>? = null) : this(
            HTLangName(enName, jaName),
            chemicalKey
        )

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

    enum class Metal(langName: HTLangName, override val chemicalKey: ResourceKey<HTChemical>?) :
        MetalLike,
        HTLangName by langName {
        // Minecraft
        COPPER("Copper", "銅", RagiumChemicals.COPPER),
        IRON("Iron", "鉄", RagiumChemicals.IRON),
        GOLD("Gold", "金", RagiumChemicals.GOLD),

        // Common

        /**
         * @since 26.1.7
         */
        ALUMINUM("Aluminum", "アルミニウム", RagiumChemicals.ALUMINUM)
        ;

        constructor(enName: String, jaName: String, chemicalKey: ResourceKey<HTChemical>? = null) : this(
            HTLangName(enName, jaName),
            chemicalKey
        )

        override val materialName: String = name.lowercase()
    }

    enum class Alloy(langName: HTLangName, override val chemicalKey: ResourceKey<HTChemical>?) :
        MetalLike,
        HTLangName by langName {
        // Minecraft
        NETHERITE("Netherite", "ネザライト"),

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
         * @since 26.1.4
         */
        VOID_METAL("Void Metal", "虚金")
        ;

        constructor(enName: String, jaName: String, chemicalKey: ResourceKey<HTChemical>? = null) : this(
            HTLangName(enName, jaName),
            chemicalKey
        )

        override val materialName: String = name.lowercase()
    }

    enum class Other(langName: HTLangName, override val chemicalKey: ResourceKey<HTChemical>?) :
        DustLike,
        HTLangName by langName {
        // Minecraft
        WOOD("Wood", "木") {
            override val isPulp: Boolean = true
        },
        GLASS("Glass", "ガラス", RagiumChemicals.SILICON_DIOXIDE),
        OBSIDIAN("Obsidian", "黒曜石"),
        PAPER("Paper", "紙") {
            override val isPulp: Boolean = true
        }
        ;

        constructor(enName: String, jaName: String, chemicalKey: ResourceKey<HTChemical>? = null) : this(
            HTLangName(enName, jaName),
            chemicalKey
        )

        open val isPulp: Boolean = false

        override val materialName: String = name.lowercase()
    }
}
