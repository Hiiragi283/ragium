package hiiragi283.ragium.api.material

import hiiragi283.lib.data.lang.HTLangPatternProvider
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTMaterialLike
import hiiragi283.lib.tag.HTTagPrefix

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
sealed interface HTPart : HTLangPatternProvider {
    val tagPrefix: HTTagPrefix

    fun createName(material: HTMaterialLike): String
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
sealed interface HTBlockPart : HTPart {
    companion object {
        @JvmField
        val entries: Sequence<HTBlockPart> = sequence {
            yieldAll(HTOreBlockPart.entries)
            yieldAll(HTStorageBlockPart.entries)
        }
    }
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
enum class HTOreBlockPart(private val idPattern: String, provider: HTLangPatternProvider) :
    HTBlockPart,
    HTLangPatternProvider by provider {
    STONE("%s_ore", "%s Ore", "%s鉱石"),
    DEEPSLATE("deepslate_%s_ore", "Deepslate %s Ore", "深層%s鉱石"),
    NETHER("nether_%s_ore", "Nether %s Ore", "ネザー%s鉱石"),
    END("end_%s_ore", "End %s Ore", "エンド%s鉱石"),
    ;

    constructor(idPattern: String, enPattern: String, jaPattern: String) : this(
        idPattern,
        HTLangPatternProvider(enPattern, jaPattern),
    )

    override val tagPrefix: HTTagPrefix = CommonTagPrefixes.ORE

    override fun createName(material: HTMaterialLike): String = idPattern.replace("%s", material.materialName)
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
enum class HTStorageBlockPart(private val idPattern: String, provider: HTLangPatternProvider) :
    HTBlockPart,
    HTLangPatternProvider by provider {
    RAW("raw_%s_block", "Block of Raw %s", "%sの原石ブロック"),
    DEFAULT("%s_block", "Block of %s", "%sブロック"),
    ;

    constructor(idPattern: String, enPattern: String, jaPattern: String) : this(
        idPattern,
        HTLangPatternProvider(enPattern, jaPattern),
    )

    override val tagPrefix: HTTagPrefix = CommonTagPrefixes.STORAGE_BLOCK

    override fun createName(material: HTMaterialLike): String = idPattern.replace("%s", material.materialName)
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
enum class HTItemPart(private val idPattern: String, override val tagPrefix: HTTagPrefix, provider: HTLangPatternProvider) :
    HTPart,
    HTLangPatternProvider by provider {
    // Ore
    RAW("raw_%s", CommonTagPrefixes.RAW_MATERIALS, "Raw %s", "%sの原石"),

    /**
     * @since 26.1.13
     */
    CRUSHED_ORES("crushed_%s_ore", CommonTagPrefixes.CRUSHED_ORES, "Crushed %s Ore", "粉砕された%s鉱石"),

    // Dust
    DUST("%s_dust", CommonTagPrefixes.DUST, "%s Dust", "%sの粉"),

    /**
     * @since 26.1.13
     */
    SMALL_DUST("small_%s_dust", CommonTagPrefixes.SMALL_DUST, "Small %s Dust", "小さな%sの粉"),

    // Base
    GEM("%s", CommonTagPrefixes.GEM, HTLangPatternProvider.IDENTITY),
    INGOT("%s_ingot", CommonTagPrefixes.INGOT, "%s Ingot", "%sインゴット"),

    // Small Base
    TINY("tiny_%s", CommonTagPrefixes.TINY, "Tiny %s", "小さな%s"),
    NUGGET("%s_nugget", CommonTagPrefixes.NUGGET, "%s Nugget", "%sナゲット"),

    // Part
    GEAR("%s_gear", CommonTagPrefixes.GEAR, "%s Gear", "%sの歯車"),
    ;

    constructor(idPattern: String, tagPrefix: HTTagPrefix, enPattern: String, jaPattern: String) : this(
        idPattern,
        tagPrefix,
        HTLangPatternProvider(enPattern, jaPattern),
    )

    override fun createName(material: HTMaterialLike): String = idPattern.replace("%s", material.materialName)
}
