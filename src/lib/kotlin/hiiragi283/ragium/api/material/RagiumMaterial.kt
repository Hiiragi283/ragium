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
            yieldAll(Mineral.entries)
            yieldAll(Gem.entries)
            yieldAll(Metal.entries)
            yieldAll(Other.entries)
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

    enum class Mineral(langName: HTLangName, override val chemicalKey: ResourceKey<HTChemical>?) :
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
        SULFUR("Sulfur", "硫黄", RagiumChemicals.SULFUR),
        NITER("Niter", "硝石"),
        BORAX("Borax", "ホウ砂"),

        /**
         * @since 26.1.7
         */
        BAUXITE("Bauxite", "ボーキサイト"),

        // Ragium
        RAGINITE("Raginite", "ラギナイト")
        ;

        constructor(enName: String, jaName: String, chemicalKey: ResourceKey<HTChemical>? = null) : this(
            HTLangName(enName, jaName),
            chemicalKey
        )

        open val isVanilla: Boolean = false

        override val materialName: String = name.lowercase()
    }

    enum class Gem(langName: HTLangName, override val chemicalKey: ResourceKey<HTChemical>?) :
        RagiumMaterial,
        HTLangName by langName {
        // Minecraft
        LAPIS("Lapis", "ラピス"),
        QUARTZ("Quartz", "水晶"),
        AMETHYST("Amethyst", "アメジスト"),
        DIAMOND("Diamond", "ダイヤモンド", RagiumChemicals.DIAMOND),
        EMERALD("Emerald", "エメラルド"),
        ECHO("Echo", "残響"),
        PRISMARINE("Prismarine", "プリズマリン"),

        // Common

        /**
         * @since 26.1.7
         */
        FLUORITE("Fluorite", "蛍石"),

        /**
         * @since 26.1.7
         */
        CRYOLITE("Cryolite", "氷晶石")
        ;

        constructor(enName: String, jaName: String, chemicalKey: ResourceKey<HTChemical>? = null) : this(
            HTLangName(enName, jaName),
            chemicalKey
        )

        override val materialName: String = name.lowercase()
    }

    enum class Metal(langName: HTLangName, override val chemicalKey: ResourceKey<HTChemical>?) :
        RagiumMaterial,
        HTLangName by langName {
        // Minecraft
        COPPER("Copper", "銅"),
        IRON("Iron", "鉄"),
        GOLD("Gold", "金"),
        NETHERITE("Netherite", "ネザライト") {
            override val hasRawVariant: Boolean = false
        },

        // Common

        /**
         * @since 26.1.7
         */
        ALUMINUM("Aluminum", "アルミニウム", RagiumChemicals.ALUMINUM),

        // Ragium

        /**
         * @since 26.1.3
         */
        SOOTY_IRON("Sooty Iron", "煤鉄") {
            override val hasRawVariant: Boolean = false
        },

        /**
         * @since 26.1.3
         */
        BLACK_STEEL("Black Steel", "黒鋼") {
            override val hasRawVariant: Boolean = false
        },

        /**
         * @since 26.1.4
         */
        VOID_METAL("Void Metal", "虚金") {
            override val hasRawVariant: Boolean = false
        }
        ;

        constructor(enName: String, jaName: String, chemicalKey: ResourceKey<HTChemical>? = null) : this(
            HTLangName(enName, jaName),
            chemicalKey
        )

        open val hasRawVariant: Boolean = true

        override val materialName: String = name.lowercase()
    }

    enum class Other(langName: HTLangName, override val chemicalKey: ResourceKey<HTChemical>?) :
        RagiumMaterial,
        HTLangName by langName {
        // Minecraft
        WOOD("Wood", "木") {
            override val isPulp: Boolean = true
        },
        GLASS("Glass", "ガラス"),
        OBSIDIAN("Obsidian", "黒曜石"),
        PAPER("Paper", "紙") {
            override val isPulp: Boolean = true
        },

        // Common
        CARBON("Carbon", "炭素", RagiumChemicals.CARBON),

        /**
         * @since 26.1.7
         */
        ALUMINA("Alumina", "アルミナ", RagiumChemicals.ALUMINA),

        /**
         * @since 26.1.7
         */
        SILICON("Silicon", "シリコン", RagiumChemicals.SILICON)
        ;

        constructor(enName: String, jaName: String, chemicalKey: ResourceKey<HTChemical>? = null) : this(
            HTLangName(enName, jaName),
            chemicalKey
        )

        open val isPulp: Boolean = false

        override val materialName: String = name.lowercase()
    }
}
