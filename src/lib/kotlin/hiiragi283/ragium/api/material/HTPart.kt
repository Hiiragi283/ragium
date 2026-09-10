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
            yield(HTStorageBlockPart)
            yield(HTRawStorageBlockPart)
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
    END("end_%s_ore", "End %s Ore", "エンド%s鉱石")
    ;

    constructor(idPattern: String, enPattern: String, jaPattern: String) : this(
        idPattern,
        HTLangPatternProvider(enPattern, jaPattern)
    )

    override val tagPrefix: HTTagPrefix = CommonTagPrefixes.ORE

    override fun createName(material: HTMaterialLike): String = idPattern.replace("%s", material.materialName)
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
data object HTStorageBlockPart :
    HTBlockPart,
    HTLangPatternProvider by HTLangPatternProvider("Block of %s", "%sブロック") {
    override val tagPrefix: HTTagPrefix = CommonTagPrefixes.STORAGE_BLOCK

    override fun createName(material: HTMaterialLike): String = "${material.materialName}_block"
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
data object HTRawStorageBlockPart :
    HTBlockPart,
    HTLangPatternProvider by HTLangPatternProvider("Block of Raw %s", "%sの原石ブロック") {
    override val tagPrefix: HTTagPrefix = CommonTagPrefixes.STORAGE_BLOCK

    override fun createName(material: HTMaterialLike): String = "raw_${material.materialName}_block"
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
enum class HTItemPart(
    private val idPattern: String,
    override val tagPrefix: HTTagPrefix,
    provider: HTLangPatternProvider
) : HTPart,
    HTLangPatternProvider by provider {
    DUST("%s_dust", CommonTagPrefixes.DUST, "%s Dust", "%sの粉"),
    GEAR("%s_gear", CommonTagPrefixes.GEAR, "%s Gear", "%sの歯車"),
    GEM("%s", CommonTagPrefixes.GEM, HTLangPatternProvider.IDENTITY),
    INGOT("%s_ingot", CommonTagPrefixes.INGOT, "%s Ingot", "%sインゴット"),
    NUGGET("%s_nugget", CommonTagPrefixes.NUGGET, "%s Nugget", "%sナゲット"),
    PLATE("%s_plate", CommonTagPrefixes.PLATE, "%s Plate", "%sの板"),
    RAW("raw_%s", CommonTagPrefixes.RAW_MATERIALS, "Raw %s", "%sの原石"),
    TINY("tiny_%s", CommonTagPrefixes.TINY, "Tiny %s", "小さな%s")
    ;

    constructor(idPattern: String, tagPrefix: HTTagPrefix, enPattern: String, jaPattern: String) : this(
        idPattern,
        tagPrefix,
        HTLangPatternProvider(enPattern, jaPattern)
    )

    override fun createName(material: HTMaterialLike): String = idPattern.replace("%s", material.materialName)
}
